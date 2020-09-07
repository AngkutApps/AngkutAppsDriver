package id.co.myproject.angkutapps.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.helper.OrderListener;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.data_access_object.DestinasiPassenger;
import id.co.myproject.angkutapps.model.FCMClient;
import id.co.myproject.angkutapps.model.data_access_object.FCMResponse;
import id.co.myproject.angkutapps.model.data_access_object.Passenger;
import id.co.myproject.angkutapps.model.data_access_object.Token;
import id.co.myproject.angkutapps.request.ApiRequest;
import id.co.myproject.angkutapps.request.DataMessage;
import id.co.myproject.angkutapps.request.GoogleMapApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends DialogFragment {
    private static final String TAG = "OrderFragment";
    
    String customer, idDestinasiPassenger, noHpUser, kodeDriver;
    LatLng latLngPassanger;

    SharedPreferences sharedPreferences;
    TextView tvNama, tvAlamat, tvWaktu, tvjarak, tvJumlahOrang,tvJumlahBarang;
    Button btnTerima, btnTolak;

    OrderListener orderListener;

    ApiRequest apiRequest;

    public OrderFragment(String customer, LatLng latLngPassanger, String idDestinasiPassenger, String noHpUser, OrderListener orderListener) {
        // Required empty public constructor
        this.customer = customer;
        this.idDestinasiPassenger = idDestinasiPassenger;
        this.noHpUser = noHpUser;
        this.latLngPassanger = latLngPassanger;
        this.orderListener = orderListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiRequest = FCMClient.getClient(Utils.fcmUrl).create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        kodeDriver = sharedPreferences.getString(Utils.KODE_DRIVER_KEY, "");


        tvNama = view.findViewById(R.id.tv_nama);
        tvAlamat = view.findViewById(R.id.tv_alamat);
        tvjarak = view.findViewById(R.id.tv_jarak);
        tvWaktu = view.findViewById(R.id.tv_waktu);
        tvJumlahOrang = view.findViewById(R.id.tv_jumlah_orang);
        tvJumlahBarang = view.findViewById(R.id.tv_jumlah_barang);
        btnTolak = view.findViewById(R.id.btn_batalkan);
        btnTerima = view.findViewById(R.id.btn_terima);

        loadDataPassenger();
        loadDetailOrder();
        loadJarak();
        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String driverToken = FirebaseInstanceId.getInstance().getToken();
                Token token = new Token(customer);
                Map<String, String> content = new HashMap<>();
                content.put("title", "Accept");
                content.put("driver_token", driverToken);
                content.put("kode_driver", kodeDriver);
                content.put("message", "Driver akan menjemput anda");
                DataMessage dataMessage = new DataMessage(token.getToken(), content);
                Log.d(TAG, "onClick: Penumpang : "+token.getToken());

                apiRequest.sendMessage(dataMessage)
                        .enqueue(new Callback<FCMResponse>() {
                            @Override
                            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                if (response.body().success == 1){
                                    orderListener.onFinishedOrder(customer, latLngPassanger, idDestinasiPassenger, noHpUser);
                                    dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<FCMResponse> call, Throwable t) {

                            }
                        });
            }
        });

        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Token token = new Token(customer);
                Map<String, String> content = new HashMap<>();
                content.put("title", "Cancel");
                content.put("message", "Driver has canceled your request");
                DataMessage dataMessage = new DataMessage(token.getToken(), content);

                apiRequest.sendMessage(dataMessage)
                        .enqueue(new Callback<FCMResponse>() {
                            @Override
                            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                if (response.body().success == 1){
                                    Toast.makeText(getActivity(), "Order dibatalkan", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<FCMResponse> call, Throwable t) {

                            }
                        });
            }
        });

    }

    private void loadJarak() {
        ApiRequest apiRequest = GoogleMapApi.getClient(Utils.mapsUrl).create(ApiRequest.class);
        String url = "https://maps.googleapis.com/maps/api/directions/json?"+
                "mode=driving&"+
                "transit_routing_preference=less_driving&"+
                "origin="+Utils.mLastLocation.getLatitude()+","+Utils.mLastLocation.getLongitude()+"&"+
                "destination="+latLngPassanger.latitude+","+latLngPassanger.longitude+"&"+
                "key="+getResources().getString(R.string.browser_api_key);
        apiRequest.getPath(url)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()){
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response.body());
                                JSONArray routes = jsonObject.getJSONArray("routes");
                                JSONObject object = routes.getJSONObject(0);
                                JSONArray legs = object.getJSONArray("legs");
                                JSONObject objectLegs = legs.getJSONObject(0);

                                JSONObject distance = objectLegs.getJSONObject("distance");
                                String distanceText = distance.getString("text");

                                JSONObject time = objectLegs.getJSONObject("duration");
                                String timeText = time.getString("text");

                                String address = objectLegs.getString("end_address");
                                tvAlamat.setText(address);
                                tvWaktu.setText(timeText);
                                tvjarak.setText(distanceText);

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

    private void loadDetailOrder() {
        FirebaseDatabase.getInstance().getReference(Utils.passenger_destination_tbl)
                .child(idDestinasiPassenger)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            DestinasiPassenger destinasiPassenger = dataSnapshot.getValue(DestinasiPassenger.class);
                            tvJumlahBarang.setText(destinasiPassenger.getJumlahBarang());
                            tvJumlahOrang.setText("Dewasa "+destinasiPassenger.getJumlahOrang().getJumlahDewasa()+", Anak "+destinasiPassenger.getJumlahOrang().getJumlahAnak());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadDataPassenger() {
        DatabaseReference dbPassanger = FirebaseDatabase.getInstance().getReference(Utils.user_passenger_tbl);
        dbPassanger.child(noHpUser)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Passenger passenger = dataSnapshot.getValue(Passenger.class);
                            tvNama.setText(passenger.getNama());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
