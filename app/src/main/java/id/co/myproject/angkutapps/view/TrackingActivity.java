package id.co.myproject.angkutapps.view;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.adapter.PerjalananAdapter;
import id.co.myproject.angkutapps.helper.DirectionJSONParser;
import id.co.myproject.angkutapps.helper.OrderListener;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.data_access_object.Destinasi;
import id.co.myproject.angkutapps.model.data_access_object.DestinasiPassenger;
import id.co.myproject.angkutapps.model.FCMClient;
import id.co.myproject.angkutapps.model.data_access_object.FCMResponse;
import id.co.myproject.angkutapps.model.data_access_object.ListPassager;
import id.co.myproject.angkutapps.model.data_access_object.Passenger;
import id.co.myproject.angkutapps.model.data_access_object.Token;
import id.co.myproject.angkutapps.request.ApiRequest;
import id.co.myproject.angkutapps.request.DataMessage;
import id.co.myproject.angkutapps.request.GoogleMapApi;
import id.co.myproject.angkutapps.view.profil.dialog_fragment.Df_chat;
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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static id.co.myproject.angkutapps.view.HomeFragment.MY_PERMISSION_REQUEST_CODE;

public class TrackingActivity extends FragmentActivity implements OnMapReadyCallback, OrderListener {

    private static final String TAG = "TrackingActivity";

    private GoogleMap mMap;
    public static final int UPDATE_INTERVAL = 0;
    public static final int FASTEST_INTERVAL = 0;
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
    ImageView imgButtonChat;

    private String destination;
    List<ListPassager> listPassagers;
    DatabaseReference tb_drivers, tb_destinasi_driver;
    GeoFire geoFire;

    Marker mCurrentMarker, markerSpesifik,markerKabupaten;
    Circle passengerMarker;
    Switch location_switch;
    boolean driverTracking = false, startTripDriver = false;
    LatLng latLngPassangerFound = null;
    SupportMapFragment mapFragment;

    //    Presense System
    DatabaseReference onlineRef, currentUserRef;

    Map<LatLng, String> mapJarak = new HashMap<>();

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
        imgButtonChat = findViewById(R.id.imgButtonChat);

        tb_destinasi_driver = FirebaseDatabase.getInstance().getReference(Utils.destination_tbl).child(kodeDriver);
        rvPerjalanan.setLayoutManager(new LinearLayoutManager(this));
        perjalananAdapter = new PerjalananAdapter(this);
        rvPerjalanan.setAdapter(perjalananAdapter);
        loadPerjalanan();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        buildLocationCallback();
        buildLocationRequest();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        }
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
                    if (ActivityCompat.checkSelfPermission(TrackingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(TrackingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(TrackingActivity.this, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        }, MY_PERMISSION_REQUEST_CODE);
                    }
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
        updateFirebaseToken();

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
                            startTripDriver = true;
                            displayLocation();
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

    private void updateFirebaseToken() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference(Utils.token_tbl);

        Token token = new Token(FirebaseInstanceId.getInstance().getToken());
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            tokens.child(kodeDriver)
                    .setValue(token);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    buildLocationCallback();
                    buildLocationRequest();
                    displayLocation();
                }
        }
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Utils.mLastLocation = location;

                        if (Utils.mLastLocation != null){
                            if (location_switch.isChecked()){
                                final double latitude = Utils.mLastLocation.getLatitude();
                                final double longitude = Utils.mLastLocation.getLongitude();

                                if (!startTripDriver) {

                                    geoFire.setLocation(kodeDriver, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                                        @Override
                                        public void onComplete(String key, DatabaseError error) {
                                            if (mCurrentMarker != null) {
                                                mCurrentMarker.remove();
                                            }

                                            if (!driverTracking) {
                                                mMap.clear();
                                            }

                                            mCurrentMarker = mMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(latitude, longitude))
                                                    .title("Your location"));
                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
                                        }
                                    });
                                    if (latLngPassangerFound != null) {
                                        if (driverTracking) {
                                            if (directionTracking != null) {
                                                directionTracking.remove();
                                            }

                                            getDirection(latLngPassangerFound);
                                        }
                                    }
                                }else {
                                    loadDestinasi();
                                }
                            }
                        }else {
                            Log.d("ERROR", "displayLocation: Cannot get your location");
                        }
                    }
                });
    }

    private void loadDestinasi() {
        Map<Location, Marker> markerKabupatenMap = new HashMap<>();
        List<Location> addresseKabupaten = new ArrayList<>();

        mMap.clear();

        mCurrentMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Utils.mLastLocation.getLatitude(), Utils.mLastLocation.getLongitude()))
                .title("Your location"));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(Utils.mLastLocation.getLatitude(), Utils.mLastLocation.getLongitude()));

        DatabaseReference db = FirebaseDatabase.getInstance().getReference(Utils.destination_tbl);
        db.child(kodeDriver).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String city, city2 = "";
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Destinasi destinasi = postSnapshot.getValue(Destinasi.class);
                    city = destinasi.getCity();

                    Address location = getLatLngFromAddress(destinasi.getAddress());

//                        builder.include(new LatLng(location.getLatitude(), location.getLongitude()));
//                        LatLngBounds bounds = builder.build();

                    markerKabupaten = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(location.getLatitude(), location.getLongitude()))
                            .title("Your Destination"));

                    Location temp = new Location(LocationManager.GPS_PROVIDER);
                    temp.setLatitude(location.getLatitude());
                    temp.setLongitude(location.getLongitude());

                    markerKabupatenMap.put(temp, markerKabupaten);
                    addresseKabupaten.add(temp);

//                        int width = getResources().getDisplayMetrics().widthPixels;
//                        int height = getResources().getDisplayMetrics().heightPixels;
//                        int padding = (int) (Math.min(width, height)*0.2);
////                                                int padding = 500; // offset from edges of the map in pixels
//                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
//
//                        mMap.moveCamera(cu);
//                        mMap.animateCamera(cu);

                    tripRadius(location, city);

//                    city2 = destinasi.getCity();
                }
                if(addresseKabupaten.contains(Utils.mLastLocation)){
                    Marker marker = markerKabupatenMap.get(Utils.mLastLocation);
                    marker.remove();
                    markerKabupatenMap.remove(Utils.mLastLocation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void urutanMarker(LatLng lokasi, String jarak){
        mapJarak.put(lokasi, jarak);
    }

    private void getJarak(LatLng titikAwal, LatLng titikTujuan){
        String requestApi = "";
        requestApi = "https://maps.googleapis.com/maps/api/directions/json?"+
                "mode=driving&"+
                "transit_routing_preference=less_driving&"+
                "origin="+titikAwal.latitude+","+titikAwal.longitude+"&"+
                "destination="+titikTujuan.latitude+","+titikTujuan.longitude+"&"+
                "key="+getResources().getString(R.string.google_direction_api);
        apiRequest.getPath(requestApi).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body());
                        JSONArray routes = jsonObject.getJSONArray("routes");
                        JSONObject object = routes.getJSONObject(0);
                        JSONArray legs = object.getJSONArray("legs");
                        JSONObject objectLegs = legs.getJSONObject(0);

                        JSONObject distance = objectLegs.getJSONObject("distance");
                        String distanceText = distance.getString("text");

                        urutanMarker(titikTujuan, distanceText);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private Address getLatLngFromAddress(String address){
        List<Address> addresses;
        Address location;
        Geocoder geocoder;
        geocoder = new Geocoder(TrackingActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocationName(address, 1);
            location = addresses.get(0);

            return location;

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }

    }


    private void tripRadius(Address location, String city) {
        GeoFire geoMarker;
        geoMarker = new GeoFire(FirebaseDatabase.getInstance().getReference(Utils.driver_tbl));
        GeoQuery geoQuery = geoMarker.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), 0.05f);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                mMap.clear();

                DatabaseReference dbPassager = FirebaseDatabase.getInstance().getReference(Utils.list_passenger_tbl);
                dbPassager.child(kodeDriver).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String hpUser = "";
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                ListPassager listPassager = postSnapshot.getValue(ListPassager.class);
                                if (!hpUser.equals(listPassager.getNo_hp_user())) {
                                    hpUser = listPassager.getNo_hp_user();
                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference(Utils.passenger_destination_tbl);
                                    db.child(listPassager.getNo_hp_user()).orderByChild("city").equalTo(city).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            List<Location> addresseTujuan = new ArrayList<>();
                                            Map<Location, Marker> markerDestinasiMap = new HashMap<>();
                                            for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                Destinasi destinasi = postSnapshot.getValue(Destinasi.class);

                                                Address location = getLatLngFromAddress(destinasi.getAddress());
                                                markerSpesifik = mMap.addMarker(new MarkerOptions()
                                                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                                        .title("Your Destination"));

                                                Location temp = new Location(LocationManager.GPS_PROVIDER);
                                                temp.setLatitude(location.getLatitude());
                                                temp.setLongitude(location.getLongitude());

                                                markerDestinasiMap.put(temp, markerSpesifik);

                                                addresseTujuan.add(temp);

                                            }

                                            if(addresseTujuan.contains(Utils.mLastLocation)){
                                                Marker marker = markerDestinasiMap.get(Utils.mLastLocation);
                                                marker.remove();
                                                markerDestinasiMap.remove(Utils.mLastLocation);
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            }
                        }else {
                            Toast.makeText(TrackingActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(TrackingActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        buildLocationRequest();
        buildLocationCallback();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        }
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
        latLngPassangerFound = latLngPassanger;
        driverTracking = true;
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
                                        imgButtonChat.setOnClickListener(v -> setFragment(new Df_chat(noHpUser)));
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
                                    Calendar calendar = Calendar.getInstance();
                                    String hari_ini  = DateFormat.format("EEEE", calendar.getTime()).toString();
                                    String tanggal = DateFormat.format("dd MMM yyyy", calendar.getTime()).toString();
                                    listPassenger.put("tanggal", hari_ini+", "+tanggal);
                                    dbListPassenger.child(idList).setValue(listPassenger)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        driverTracking = false;
                                                        sendMessageAngkut(customer, "Angkut", idList);
                                                        Toast.makeText(TrackingActivity.this, "Berhasil menambah penumpang", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            });
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sendMessageAngkut(customer, "CancelAngkut", "");
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

    private void sendMessageAngkut(String customer, String title, String idList) {
        Token token = new Token(customer);
        String driverToken = FirebaseInstanceId.getInstance().getToken();
        Map<String, String> content = new HashMap<>();
        content.put("title", title);
        content.put("kode_driver", kodeDriver);
        content.put("driver_token", driverToken);
        content.put("id_list", idList);
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

    private void setFragment(DialogFragment fragment){
//        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("dialog");
        if (prev !=null){
            fragmentTransaction.remove(prev);
        }
        fragment.show(fragmentTransaction, "dialog");
    }
}
