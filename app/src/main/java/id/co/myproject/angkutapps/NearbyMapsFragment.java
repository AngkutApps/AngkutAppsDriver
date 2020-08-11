package id.co.myproject.angkutapps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import id.co.myproject.angkutapps.helper.CostumWindowInfo;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.data_access_object.Penjemputan;
import id.co.myproject.angkutapps.model.data_access_object.Penumpang;
import id.co.myproject.angkutapps.model.data_access_object.Value;
import id.co.myproject.angkutapps.request.ApiRequest;
import id.co.myproject.angkutapps.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NearbyMapsFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private MapView mapView;
    TextView tvFull;
    Button btnPerjalanan;
    EditText etTempat;
    CardView cvPerjalanan;
    private static final int MY_PERMISSION_REQUEST_CODE = 7192;
    private static final int PLAY_SERVICE_REQUEST_CODE = 300193;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000;
    private static int FATEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    boolean isJemputanFound = false;
    String penumpangId = "";
    String tujuan = "";
    int radius = 1;
    int distance = 1;
    public static final int LIMIT = 3;


    DatabaseReference drivers;
    GeoFire geoFire;
    Marker mUserMarker;
    SharedPreferences sharedPreferences;
    ApiRequest apiRequest;
    int idDriver;

    LinearLayout lvPenjemputan;
    TextView tvNama, tvNoHp, tvBarang, tvJumlah;
    Button btnAngkut, btnTelpon;
    TextView tvMencari;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby_maps, container, false);
//        mapView = (MapView) view.findViewById(R.id.map);
//        mapView.getMapAsync(this);
//        mapView.onCreate(getArguments());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        idDriver = 0;
        tvFull = getActivity().findViewById(R.id.tv_full);
        btnPerjalanan = getActivity().findViewById(R.id.btn_perjalanan);
        etTempat = getActivity().findViewById(R.id.et_tempat);
        cvPerjalanan = getActivity().findViewById(R.id.cv_perjalanan);

        lvPenjemputan = getActivity().findViewById(R.id.lv_penjemputan);
        tvNama = getActivity().findViewById(R.id.tv_nama);
        tvNoHp = getActivity().findViewById(R.id.tv_no_hp);
        tvBarang = getActivity().findViewById(R.id.tv_barang);
        tvJumlah = getActivity().findViewById(R.id.tv_jumlah);
        btnAngkut = getActivity().findViewById(R.id.btn_angkut);
        tvMencari = getActivity().findViewById(R.id.tv_mencari);
        progressBar = getActivity().findViewById(R.id.progres);
        btnTelpon = getActivity().findViewById(R.id.btn_telepon);

        drivers = FirebaseDatabase.getInstance().getReference("Driver");
        geoFire = new GeoFire(drivers);

        setUpLocation();


        btnPerjalanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPenjemputan(idDriver);
            }
        });

    }

    private void requestPenjemputan(int idDriver) {
        GeoFire geoFire = new GeoFire(drivers);
        geoFire.setLocation(String.valueOf(idDriver), new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

        if (mUserMarker.isVisible()){
            mUserMarker.remove();
        }
        mUserMarker = mMap.addMarker(new MarkerOptions().title("Your Location").snippet("").position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mUserMarker.showInfoWindow();


        tujuan = etTempat.getText().toString();
        cvPerjalanan.setVisibility(View.GONE);
        tvFull.setVisibility(View.VISIBLE);

        findDriver();
        loadAvailableJemputan();
    }

    private void findDriver() {
        DatabaseReference drivers = FirebaseDatabase.getInstance().getReference("Jemput");
        GeoFire gfDriver = new GeoFire(drivers);

        GeoQuery geoQuery = gfDriver.queryAtLocation(new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!isJemputanFound) {
                    isJemputanFound = true;
                    penumpangId = key;
//                    btnPickupRequest.setText("CALL DRIVER");
                    Toast.makeText(getActivity(), "" + key, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!isJemputanFound) {
                    radius++;
                    findDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.setInfoWindowAdapter(new CustomInfoWindow(this));
    }

    private void setUpLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        }else {
            if (checkPlayServices()){
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null){
            final double latitude = mLastLocation.getLatitude();
            final double longitude = mLastLocation.getLongitude();
            if (mUserMarker != null){
                mUserMarker.remove();
            }
            mUserMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("Your Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
            loadAvailableJemputan();
        }else {
            Log.d("ERROR", "displayLocation: Cannot get your location");
        }
    }

    private void loadAvailableJemputan() {
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference("Jemput");
        GeoFire gf = new GeoFire(driverLocation);
        GeoQuery geoQuery = gf.queryAtLocation(new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), distance);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Call<List<Penjemputan>> callPenjemputan = apiRequest.getPenjemputan(tujuan);
                callPenjemputan.enqueue(new Callback<List<Penjemputan>>() {
                    @Override
                    public void onResponse(Call<List<Penjemputan>> call, Response<List<Penjemputan>> response) {
                        if (response.isSuccessful()){
                            List<Penjemputan> penjemputanList = response.body();
                            for (Penjemputan penjemputan : penjemputanList){
                                if (penjemputan.getIdPenumpang().equals(key)){
                                    Toast.makeText(getActivity(), "id penumpang : "+key, Toast.LENGTH_SHORT).show();
                                    Call<Penumpang> callPenumpang = apiRequest.getPenumpangById(key);
                                    callPenumpang.enqueue(new Callback<Penumpang>() {
                                        @Override
                                        public void onResponse(Call<Penumpang> call, Response<Penumpang> response) {
                                            if (response.isSuccessful()){
                                                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                                                Penumpang penumpang = response.body();
                                                mMap.addMarker(new MarkerOptions()
                                                        .position(new LatLng(location.latitude, location.longitude))
                                                        .flat(true)
                                                        .title(penumpang.getNama())
                                                        .snippet(penumpang.getNoHp()));
                                                mMap.setInfoWindowAdapter(new CostumWindowInfo(getActivity()));
                                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                                    @Override
                                                    public void onInfoWindowClick(Marker marker) {
                                                        tvFull.setVisibility(View.GONE);
                                                        lvPenjemputan.setVisibility(View.VISIBLE);
                                                        tvNama.setText("Nama : "+penumpang.getNama());
                                                        tvNoHp.setText("No Hp : "+penumpang.getNoHp());
                                                        tvBarang.setText("Barang bawaan : "+penjemputan.getBarangBawaan());
                                                        tvJumlah.setText("Jumlah : "+penjemputan.getJumlahOrang());
                                                        btnAngkut.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                                                progressDialog.setMessage("Proses angkut ...");
                                                                progressDialog.show();
                                                                btnTelpon.setVisibility(View.VISIBLE);
                                                                DatabaseReference dbReferncesAngkutPenumpang = FirebaseDatabase.getInstance().getReference("Angkut").child(String.valueOf(penumpang.getIdPenumpang()));
                                                                GeoFire geoFire = new GeoFire(dbReferncesAngkutPenumpang);
                                                                geoFire.setLocation(String.valueOf(idDriver), new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

                                                                DatabaseReference dbReferncesAngkutDriver = FirebaseDatabase.getInstance().getReference("Angkut").child(String.valueOf(idDriver));
                                                                GeoFire geoFirePenumpang = new GeoFire(dbReferncesAngkutDriver);
                                                                geoFirePenumpang.setLocation(penumpang.getIdPenumpang(), new GeoLocation(marker.getPosition().latitude, marker.getPosition().longitude));
                                                                progressDialog.dismiss();
                                                                Toast.makeText(getActivity(), "Angkutan bertambah", Toast.LENGTH_SHORT).show();
                                                                btnAngkut.setVisibility(View.GONE);
                                                                tvMencari.setVisibility(View.VISIBLE);
                                                                progressBar.setVisibility(View.VISIBLE);
                                                            }
                                                        });
                                                        LatLng latLngDriver = new LatLng(mUserMarker.getPosition().latitude, mUserMarker.getPosition().longitude);
                                                        LatLng latLngUser = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                                                        if (latLngDriver.equals(latLngUser)){
                                                            Toast.makeText(getActivity(), "Cobaahh", Toast.LENGTH_SHORT).show();
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                            builder.setTitle("Anda telah sampai");
                                                            builder.setMessage("Apakah anda ingin menjemput lagi ?");
                                                            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    lvPenjemputan.setVisibility(View.GONE);
                                                                    tvFull.setVisibility(View.VISIBLE);
                                                                    Call<Value> callRiwayat = apiRequest.inputRiwayatRequest(penjemputan.getIdKeberangkatan(), idDriver);
                                                                    callRiwayat.enqueue(new Callback<Value>() {
                                                                        @Override
                                                                        public void onResponse(Call<Value> call, Response<Value> response) {
                                                                            if (response.isSuccessful()){
                                                                                Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                if(response.body().getValue() == 1){

                                                                                }
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Call<Value> call, Throwable t) {

                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }else {
                                                Toast.makeText(getActivity(), "Gagal Penumpang", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Penumpang> call, Throwable t) {
                                            Toast.makeText(getActivity(), "Penumpang : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Penjemputan>> call, Throwable t) {
                        Toast.makeText(getActivity(), "Penjemputan : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if(distance <= LIMIT){
                    distance++;
                    loadAvailableJemputan();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS){
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICE_REQUEST_CODE);
            }else {
                Toast.makeText(getActivity(), "This device is not supported", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            return false;
        }
        return true;
    }
}
