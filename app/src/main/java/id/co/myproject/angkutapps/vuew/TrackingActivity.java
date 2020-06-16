package id.co.myproject.angkutapps.vuew;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.adapter.DestinasiAdapter;
import id.co.myproject.angkutapps.adapter.PerjalananAdapter;
import id.co.myproject.angkutapps.helper.DirectionJSONParser;
import id.co.myproject.angkutapps.helper.OrderListener;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.Destinasi;
import id.co.myproject.angkutapps.model.DestinasiPassenger;
import id.co.myproject.angkutapps.model.FCMClient;
import id.co.myproject.angkutapps.model.FCMResponse;
import id.co.myproject.angkutapps.model.Passenger;
import id.co.myproject.angkutapps.model.Token;
import id.co.myproject.angkutapps.request.ApiRequest;
import id.co.myproject.angkutapps.request.DataMessage;
import id.co.myproject.angkutapps.request.GoogleMapApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.maps.android.SphericalUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackingActivity extends FragmentActivity implements OnMapReadyCallback, OrderListener {

    private GoogleMap mMap;
    public static final int UPDATE_INTERVAL = 5000;
    public static final int FASTEST_INTERVAL = 3000;
    public static final int DISPLACEMENT = 10;

    private Polyline directionTracking;
    ApiRequest apiRequest;

    SharedPreferences sharedPreferences;
    String kodeDriver;
    RecyclerView rvPerjalanan;
    CardView cvPerjalanan;
    PerjalananAdapter perjalananAdapter;

    private LocationRequest mLocationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;

    TextView tvNamaPassenger, tvJumlahOrang, tvJumlahBarang, tvFull;
    Button btnCancel, btnAngkut;
    LinearLayout lvActionJemput, lvJemput, lvPenjemputan;

    private String destination;
    DatabaseReference tb_drivers, tb_destinasi_driver;
    GeoFire geoFire;


    Marker mCurrentMarker;
    Circle passengerMarker;
    Switch location_switch;
    SupportMapFragment mapFragment;

    //    Presense System
    DatabaseReference onlineRef, currentUserRef;

    private BroadcastReceiver mOrderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String customer = intent.getStringExtra("customer");
            String lat = intent.getStringExtra("lat");
            String lng = intent.getStringExtra("lng");
            String idDestinasiPassenger = intent.getStringExtra("id_destinasi");
            String noHpUser = intent.getStringExtra("no_hp_user");

            LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            FragmentManager fm = getSupportFragmentManager();
            OrderFragment orderFragment = new OrderFragment(customer, latLng, idDestinasiPassenger, noHpUser, TrackingActivity.this::onFinishedOrder);
            orderFragment.show(fm, "");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        apiRequest = GoogleMapApi.getClient(Utils.mapsUrl).create(ApiRequest.class);

        sharedPreferences = getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        kodeDriver = sharedPreferences.getString(Utils.KODE_DRIVER_KEY, "");
        rvPerjalanan = findViewById(R.id.rv_perjalanan);
        cvPerjalanan = findViewById(R.id.cv_perjalanan);
        lvPenjemputan = findViewById(R.id.lv_penjemputan);
        lvJemput = findViewById(R.id.lv_jemput);
        lvActionJemput = findViewById(R.id.lv_action_jemput);
        tvNamaPassenger = findViewById(R.id.tv_nama_passenger);
        tvJumlahBarang = findViewById(R.id.tv_jumlah_barang);
        tvJumlahOrang = findViewById(R.id.tv_jumlah_orang);
        btnAngkut = findViewById(R.id.btn_angkut);
        btnCancel = findViewById(R.id.btn_cancel);
        tvFull = findViewById(R.id.tv_full);

        tb_destinasi_driver = FirebaseDatabase.getInstance().getReference(Utils.destination_tbl).child(kodeDriver);
        rvPerjalanan.setLayoutManager(new LinearLayoutManager(this));
        perjalananAdapter = new PerjalananAdapter(this);
        rvPerjalanan.setAdapter(perjalananAdapter);
        loadPerjalanan();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        buildLocationCallback();
        buildLocationRequest();
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());

        tb_drivers = FirebaseDatabase.getInstance().getReference(Utils.driver_tbl);
        geoFire = new GeoFire(tb_drivers);
        displayLocation();

        location_switch = findViewById(R.id.loc_switch);
        location_switch.setChecked(true);
        location_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isOnline) {
                if (isOnline){
                    FirebaseDatabase.getInstance().goOnline();
                    buildLocationCallback();
                    buildLocationRequest();
                    fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());

                    tb_drivers = FirebaseDatabase.getInstance().getReference(Utils.driver_tbl);
                    geoFire = new GeoFire(tb_drivers);
                    displayLocation();
                    Snackbar.make(mapFragment.getView(), "You are online", Snackbar.LENGTH_SHORT)
                            .show();

                }else {
                    FirebaseDatabase.getInstance().goOffline();

                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);

                    mCurrentMarker.remove();
                    mMap.clear();

                    Snackbar.make(mapFragment.getView(), "You are offline", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });

        setUpLocation();

        tvFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbDriver = FirebaseDatabase.getInstance().getReference(Utils.user_driver_tbl).child(kodeDriver);
                Map<String, Object> updateStatus = new HashMap<>();
                updateStatus.put("status", "1");
                dbDriver.updateChildren(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            tvFull.setVisibility(View.GONE);
                            Toast.makeText(TrackingActivity.this, "Mobil anda telah penuh", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(TrackingActivity.this, "Gagal penuh", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mOrderReceiver, new IntentFilter(Utils.ORDER__RECEIVER));
    }

    private void loadPerjalanan() {
        tb_destinasi_driver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    List<Destinasi> destinasiList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Destinasi destinasi = snapshot.getValue(Destinasi.class);
                        destinasiList.add(destinasi);
                    }
                    perjalananAdapter.setDestinasiList(destinasiList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpLocation() {
        buildLocationCallback();
        buildLocationRequest();
        if (location_switch.isChecked()){
            tb_drivers = FirebaseDatabase.getInstance().getReference(Utils.driver_tbl);
        }
    }

    private void buildLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()){
                    Utils.mLastLocation = location;
                }
                displayLocation();
            }
        };
    }

    private void displayLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Utils.mLastLocation = location;

                        if (Utils.mLastLocation != null){
                            if (location_switch.isChecked()){
                                final double latitude = Utils.mLastLocation.getLatitude();
                                final double longitude = Utils.mLastLocation.getLongitude();


                                geoFire.setLocation(kodeDriver, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                                    @Override
                                    public void onComplete(String key, DatabaseError error) {
                                        if (mCurrentMarker != null){
                                            mCurrentMarker.remove();
                                        }

                                        mMap.clear();

                                        mCurrentMarker = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .title("Your location"));
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
                                    }
                                });
                            }
                        }else {
                            Log.d("ERROR", "displayLocation: Cannot get your location");
                        }
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        buildLocationRequest();
        buildLocationCallback();
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mOrderReceiver);
    }

    @Override
    public void onFinishedOrder(String customer, LatLng latLngPassanger, String idDestinasiPassenger, String noHpUser) {
        tvFull.setVisibility(View.GONE);
        passengerMarker = mMap.addCircle(new CircleOptions()
                .center(new LatLng(latLngPassanger.latitude, latLngPassanger.longitude))
                .radius(50)
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF)
                .strokeWidth(5.0f));
        loadDataPenjemputan(noHpUser, idDestinasiPassenger);
        GeoFire geoTracking;
        geoTracking = new GeoFire(FirebaseDatabase.getInstance().getReference(Utils.driver_tbl));
        GeoQuery geoQuery = geoTracking.queryAtLocation(new GeoLocation(latLngPassanger.latitude, latLngPassanger.longitude), 0.05f);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                sendArrivedNotification(customer, idDestinasiPassenger, noHpUser);
//                Todo :
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

        if (directionTracking != null){
            directionTracking.remove();
        }

        getDirection(latLngPassanger);
    }

    private void loadDataPenjemputan(String noHpUser, String idDestinasi) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        Toast.makeText(this, "Nohp User: "+noHpUser, Toast.LENGTH_SHORT).show();
        DatabaseReference dbPenumpang = FirebaseDatabase.getInstance().getReference(Utils.user_passenger_tbl).child(noHpUser);
        dbPenumpang.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Passenger passenger = dataSnapshot.getValue(Passenger.class);
                    tvNamaPassenger.setText(passenger.getNama());
                    DatabaseReference dbBarang = FirebaseDatabase.getInstance().getReference(Utils.passenger_destination_tbl).child(noHpUser);
                    dbBarang.child(idDestinasi)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        DestinasiPassenger destinasiPassenger = dataSnapshot.getValue(DestinasiPassenger.class);
                                        tvJumlahOrang.setText(destinasiPassenger.getJumlahOrang().getJumlahDewasa() + " Dewasa, " + destinasiPassenger.getJumlahOrang().getJumlahAnak() + " Anak");
                                        tvJumlahBarang.setText(destinasiPassenger.getJumlahBarang());
                                        lvPenjemputan.setVisibility(View.VISIBLE);
                                        progressDialog.dismiss();
                                    }else {
                                        Toast.makeText(TrackingActivity.this, "Gagal tampil jemput", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }else {
                    Toast.makeText(TrackingActivity.this, "Gagal tampil user", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDirection(LatLng latLngPassanger) {
        LatLng currentPosition = new LatLng(Utils.mLastLocation.getLatitude(), Utils.mLastLocation.getLongitude());
        String requestApi = null;
        try{
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?"+
                    "mode=driving&"+
                    "transit_routing_preference=less_driving&"+
                    "origin="+currentPosition.latitude+","+currentPosition.longitude+"&"+
                    "destination="+latLngPassanger.latitude+","+latLngPassanger.longitude+"&"+
                    "key="+getResources().getString(R.string.google_direction_api);
            Log.d("MANTAP DJIWA URL : ",requestApi);
            apiRequest.getPath(requestApi).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        new ParserTask().execute(response.body().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(TrackingActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendArrivedNotification(String customer, String idDestinasi, String noHpUser){
        Token token = new Token(customer);
        String driverToken = FirebaseInstanceId.getInstance().getToken();
        Map<String, String> content = new HashMap<>();
        content.put("title", "Arrived");
        content.put("driver_token", driverToken);
        content.put("message", "Driver telah sampai di lokasi anda");
        DataMessage dataMessage = new DataMessage(token.getToken(), content);
        ApiRequest fcmRequest = FCMClient.getClient(Utils.fcmUrl).create(ApiRequest.class);
        fcmRequest.sendMessage(dataMessage)
                .enqueue(new Callback<FCMResponse>() {
                    @Override
                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                        if (response.body().success == 1){
                            lvJemput.setVisibility(View.GONE);
                            lvActionJemput.setVisibility(View.VISIBLE);
                            btnAngkut.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference dbListPassenger = FirebaseDatabase.getInstance().getReference(Utils.list_passenger_tbl)
                                            .child(kodeDriver);
                                    String idList = dbListPassenger.push().getKey();
                                    Map<String, Object> listPassenger = new HashMap<>();
                                    listPassenger.put("id_list", idList);
                                    listPassenger.put("id_destinasi", idDestinasi);
                                    listPassenger.put("no_hp_user", noHpUser);
                                    dbListPassenger.child(idList).setValue(listPassenger)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        sendMessageAngkut(customer, "Angkut");
                                                        Toast.makeText(TrackingActivity.this, "Berhasil menambah penumpang", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            });
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sendMessageAngkut(customer, "CancelAngkut");
                                    Toast.makeText(TrackingActivity.this, "Anda telah membatalkan orderan", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            Toast.makeText(TrackingActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FCMResponse> call, Throwable t) {

                    }
                });
    }

    private void sendMessageAngkut(String customer, String title) {
        Token token = new Token(customer);
        String driverToken = FirebaseInstanceId.getInstance().getToken();
        Map<String, String> content = new HashMap<>();
        content.put("title", title);
        content.put("driver_token", driverToken);
        content.put("message", "");
        DataMessage dataMessage = new DataMessage(token.getToken(), content);
        ApiRequest fcmRequest = FCMClient.getClient(Utils.fcmUrl).create(ApiRequest.class);
        fcmRequest.sendMessage(dataMessage)
                .enqueue(new Callback<FCMResponse>() {
                    @Override
                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                        if (response.body().success == 1){
                            lvPenjemputan.setVisibility(View.GONE);
                            displayLocation();
                            tvFull.setVisibility(View.VISIBLE);
                        }else {
                            Toast.makeText(TrackingActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FCMResponse> call, Throwable t) {

                    }
                });
    }


    public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        ProgressDialog mDialog = new ProgressDialog(TrackingActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.setMessage("Please waiting ...");
            mDialog.show();
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionJSONParser parser = new DirectionJSONParser();
                routes = parser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);
            mDialog.dismiss();
            ArrayList points = null;
            PolylineOptions polylineOptions = null;

            for (int i=0; i<lists.size(); i++){
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = lists.get(i);

                for (int k=0; k<path.size(); k++){
                    HashMap<String, String> point = path.get(k);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));

                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                polylineOptions.addAll(points);
                polylineOptions.width(10);
                polylineOptions.color(Color.RED);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions != null){
                directionTracking = mMap.addPolyline(polylineOptions);
            }
        }
    }
}