package id.co.myproject.angkutapps.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.adapter.DestinasiAdapter;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.data_access_object.Destinasi;
import id.co.myproject.angkutapps.model.data_access_object.Token;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;

    public static final int MY_PERMISSION_REQUEST_CODE = 7000;
    public static final int UPDATE_INTERVAL = 5000;
    public static final int FASTEST_INTERVAL = 3000;
    public static final int DISPLACEMENT = 10;

    SharedPreferences sharedPreferences;
    String kodeDriver;
    DestinasiAdapter destinasiAdapter;
    RecyclerView rvDestinasi;
    CardView cvDestinasi;
    Button btnMulaiPerjalanan;

    private LocationRequest mLocationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    private AutocompleteSupportFragment autocompleteSupportFragment;

    private String destination;
    DatabaseReference tb_drivers, tb_destinasi_driver;
    GeoFire geoFire;

    Marker mCurrentMarker;
    SupportMapFragment mapFragment;

    //    Presense System
    DatabaseReference onlineRef, currentUserRef;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sharedPreferences = getActivity().getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        kodeDriver = sharedPreferences.getString(Utils.KODE_DRIVER_KEY, "");
        rvDestinasi = getActivity().findViewById(R.id.rv_destinasi);
        cvDestinasi = getActivity().findViewById(R.id.cv_destinasi);
        btnMulaiPerjalanan = getActivity().findViewById(R.id.btn_mulai_perjalanan);

        rvDestinasi.setLayoutManager(new LinearLayoutManager(getActivity()));

        tb_destinasi_driver = FirebaseDatabase.getInstance().getReference(Utils.destination_tbl).child(kodeDriver);
        destinasiAdapter = new DestinasiAdapter(getActivity(), tb_destinasi_driver);
        rvDestinasi.setAdapter(destinasiAdapter);
        loadDestinasi();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        buildLocationCallback();
        buildLocationRequest();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        }
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());

        tb_drivers = FirebaseDatabase.getInstance().getReference(Utils.driver_tbl);
        geoFire = new GeoFire(tb_drivers);
        displayLocation();

        DatabaseReference dbDriver = FirebaseDatabase.getInstance().getReference(Utils.user_driver_tbl).child(kodeDriver);
        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put("status", "0");
        dbDriver.updateChildren(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    setUpLocation();
                    updateFirebaseToken();
                }
            }
        });


        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), getString(R.string.google_api_key));
        }
        autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.place_location);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        autocompleteSupportFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteSupportFragment.setCountry("IDN");
        autocompleteSupportFragment.setHint("Anda mau ke mana hari ini ?");
        EditText etPlace = (EditText) autocompleteSupportFragment.getView().findViewById(R.id.places_autocomplete_search_input);
        etPlace.setTextSize(12.0f);
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                destination = place.getAddress();
                destination = destination.replace(" ", "+");
//                    Toast.makeText(getActivity(), ""+destination, Toast.LENGTH_SHORT).show();
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    LatLng latLng = place.getLatLng();
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    String city = addresses.get(0).getSubAdminArea();
                    autocompleteSupportFragment.setHint(place.getAddress());

                    String idDestinasi = tb_destinasi_driver.push().getKey();
                    Map<String, Object> destinastionDriver = new HashMap<>();
                    destinastionDriver.put("idDestinasi", idDestinasi);
                    destinastionDriver.put("address", place.getAddress());
                    destinastionDriver.put("city", city);
                    tb_destinasi_driver.child(idDestinasi).setValue(destinastionDriver)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        loadDestinasi();
                                    } else {
                                        Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getActivity(), "" + status.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btnMulaiPerjalanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tb_destinasi_driver.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Intent intent = new Intent(getActivity(), TrackingActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "Masukkan destinasi anda terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void loadDestinasi() {
        tb_destinasi_driver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Destinasi> destinasiList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Destinasi destinasi = snapshot.getValue(Destinasi.class);
                        destinasiList.add(destinasi);
                    }
                    destinasiAdapter.setDestinasiList(destinasiList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buildLocationCallback();
                    buildLocationRequest();
                    displayLocation();
                }
        }
    }

    private void setUpLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {
            buildLocationCallback();
            buildLocationRequest();
            tb_drivers = FirebaseDatabase.getInstance().getReference(Utils.driver_tbl);
        }
    }

    private void updateFirebaseToken() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference(Utils.token_tbl);

        Token token = new Token(FirebaseInstanceId.getInstance().getToken());
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
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
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Utils.mLastLocation = location;
                }
                displayLocation();
            }
        };
    }

    private void displayLocation() {
//        if (ActivityCompat.checkSelfPermission((Activity)getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission((Activity)getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//        return;
//        }

//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Utils.mLastLocation = location;

                        if (Utils.mLastLocation != null) {
                            final double latitude = Utils.mLastLocation.getLatitude();
                            final double longitude = Utils.mLastLocation.getLongitude();

                            LatLng center = new LatLng(Utils.mLastLocation.getLatitude(), Utils.mLastLocation.getLongitude());
                            LatLng northSide = SphericalUtil.computeOffset(center, 500, 0);
                            LatLng southSide = SphericalUtil.computeOffset(center, 500, 180);

                            LatLngBounds bounds = LatLngBounds.builder()
                                    .include(northSide)
                                    .include(southSide)
                                    .build();

                            autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(bounds));


                            geoFire.setLocation(kodeDriver, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    if (mCurrentMarker != null) {
                                        mCurrentMarker.remove();
                                    }

                                    mCurrentMarker = mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(latitude, longitude))
                                            .title("Your location"));
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
                                }
                            });
                        } else {
                            Log.d("ERROR", "displayLocation: Cannot get your location");
                        }
                    }
                });
    }

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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        }
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
    }
}
