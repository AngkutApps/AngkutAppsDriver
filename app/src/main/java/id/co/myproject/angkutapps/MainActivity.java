package id.co.myproject.angkutapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.data_access_object.DataDriver;
import id.co.myproject.angkutapps.model.data_access_object.Driver;
import id.co.myproject.angkutapps.request.ApiRequest;
import id.co.myproject.angkutapps.request.ApiRequestDataDriver;
import id.co.myproject.angkutapps.request.RetrofitRequest;
import id.co.myproject.angkutapps.view.HomeFragment;
import id.co.myproject.angkutapps.view.history.HistoryFragment;
import id.co.myproject.angkutapps.view.profil.ProfilFragment;
import id.co.myproject.angkutapps.view.profil.ProfilUser;
import id.co.myproject.angkutapps.view.promo.PromoFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    String kodeDriver, noHpUser;
    ApiRequest apiRequest;
    SharedPreferences sharedPreferences;

    FrameLayout frameLayout;
    public static BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        kodeDriver = sharedPreferences.getString(Utils.KODE_DRIVER_KEY, "");
        noHpUser = sharedPreferences.getString(Utils.NOHP_DRIVER_KEY, "");

        refreshDataProfil();

        frameLayout = findViewById(R.id.frame_home);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home_nav){
                    setFragment(new HomeFragment());
//                    setFragment(new NearbyMapsFragment());
                }else if (item.getItemId() == R.id.history_nav){
                    setFragment(new HistoryFragment());
                }else if (item.getItemId() == R.id.voucher_nav){
                    setFragment(new PromoFragment());
                }else if (item.getItemId() == R.id.akun_nav){
                    setFragment(new ProfilFragment());
                }

                return true;
            }
        });

        setFragment(new HomeFragment());

    }

    private void refreshDataProfil(){
        Call<List<DataDriver>> call = ApiRequestDataDriver.getInstance().getApi().getDataDriver(noHpUser);

        call.enqueue(new Callback<List<DataDriver>>() {
            @Override
            public void onResponse(Call<List<DataDriver>> call, Response<List<DataDriver>> response) {

//                Log.d(TAG, "onResponse: Mantap Djiwa : "+response.body().size());
                DataDriver data = response.body().get(0);
                Driver driver = new Driver();
                driver.setKodeDriver(kodeDriver);
                driver.setNama(data.getNama_driver());
                driver.setEmail(data.getEmail());
                driver.setNoHp(noHpUser);
                driver.setJk(String.valueOf(data.getJenis_kelamin()));
                driver.setMerkMobil(data.getMerk_mobil());
                driver.setIdJenisKendaraan("1");
                driver.setPlat(data.getPlat_mobil());
                driver.setStatus("0");
                DatabaseReference dbDriver = FirebaseDatabase.getInstance().getReference(Utils.user_driver_tbl);
                dbDriver.child(kodeDriver).setValue(driver)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "onComplete: Berhasil");
                                }else {
                                    Log.d(TAG, "onComplete: Gagal");
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: gagal firbase");
                            }
                        });

            }

            @Override
            public void onFailure(Call<List<DataDriver>> call, Throwable t) {
//                progressDialog.dismiss();
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(), fragment);
        transaction.commit();
    }


    @Override
    protected void onStart() {
        super.onStart();
        refreshDataProfil();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDataProfil();
    }
}
