package id.co.myproject.angkutapps.view.profil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;

import java.util.ArrayList;
import java.util.List;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.adapter.rv_kontak_darurat;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.crud_table.tb_kontak_darurat_user;
import id.co.myproject.angkutapps.model.data_access_object.LoadKontakDarurat;
import id.co.myproject.angkutapps.request.ApiRequestKontakDarurat;
import id.co.myproject.angkutapps.view.profil.dialog_fragment.Df_tambah_kontak_darurat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KontakDarurat extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    ImageView img_back;
    CardView cvTambahKontakDarurat;
    RecyclerView rv_kontak_darurat;

    rv_kontak_darurat kontakDaruratAdapter;
    tb_kontak_darurat_user crudKontakDarurat;
    String noHpUser;
    ProgressDialog progressDialog;

    List<LoadKontakDarurat> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontak_darurat);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Tunggu Sebentar....");
        progressDialog.show();

        img_back = findViewById(R.id.img_back);
        cvTambahKontakDarurat = findViewById(R.id.cvTambahKontakDarurat);

//        AndroidNetworking.initialize(KontakDarurat.this);
        sharedPreferences = getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        noHpUser = sharedPreferences.getString(Utils.NOHP_DRIVER_KEY, "");
//        crudKontakDarurat = new tb_kontak_darurat_user(this, noHpUser);

        rv_kontak_darurat = findViewById(R.id.rvKontakDarurat);
        rv_kontak_darurat.setLayoutManager(new LinearLayoutManager(KontakDarurat.this));
        rv_kontak_darurat.setHasFixedSize(true);

        cvTambahKontakDarurat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new Df_tambah_kontak_darurat());
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        readData(noHpUser);

    }

    private void setFragment(DialogFragment fragment) {
        FragmentManager fragmentManager = KontakDarurat.this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("dialog");
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragment.show(fragmentTransaction, "dialog");
    }

    public void readData(String noHpUser) {
        arrayList = new ArrayList<>();

        Call<List<LoadKontakDarurat>> call = ApiRequestKontakDarurat.getInstance().getApi().getKontakDarurat(noHpUser);
        call.enqueue(new Callback<List<LoadKontakDarurat>>() {
            @Override
            public void onResponse(Call<List<LoadKontakDarurat>> call, Response<List<LoadKontakDarurat>> response) {
                arrayList = response.body();

//                Log.i("Adakah", ""+response.body());
                kontakDaruratAdapter = new rv_kontak_darurat(KontakDarurat.this, arrayList, noHpUser);
                rv_kontak_darurat.setAdapter(kontakDaruratAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<LoadKontakDarurat>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(KontakDarurat.this, "Data Gagal Terbaca", Toast.LENGTH_SHORT).show();
            }
        });
    }

}