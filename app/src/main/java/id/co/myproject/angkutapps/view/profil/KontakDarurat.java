package id.co.myproject.angkutapps.view.profil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;

import java.util.List;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.crud_table.tb_kontak_darurat_user;
import id.co.myproject.angkutapps.view.profil.dialog_fragment.Df_tambah_kontak_darurat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KontakDarurat extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    ImageView img_back;
    CardView cvTambahKontakDarurat;

    tb_kontak_darurat_user crudKontakDarurat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontak_darurat);

        img_back = findViewById(R.id.img_back);
        cvTambahKontakDarurat = findViewById(R.id.cvTambahKontakDarurat);

//        AndroidNetworking.initialize(KontakDarurat.this);
//        sharedPreferences = getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
//        String noHpUser = sharedPreferences.getString(Utils.NOHP_DRIVER_KEY, "");
//        crudKontakDarurat = new tb_kontak_darurat_user(this, noHpUser);

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

//    public void readData(String noHpUser) {
//        Call<List<LoadKontakDarurat>> call = request_tb_kontak_darurat.getInstance().getApi().getKontakDarurat(noHpUser);
//        call.enqueue(new Callback<List<LoadKontakDarurat>>() {
//            @Override
//            public void onResponse(Call<List<LoadKontakDarurat>> call, Response<List<LoadKontakDarurat>> response) {
//                arrayList = response.body();
//
//                kontakDaruratAdapter = new rv_profil_kontak_darurat(KontakDarurat.this, arrayList);
//                rvKontakDarurat.setAdapter(kontakDaruratAdapter);
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<List<LoadKontakDarurat>> call, Throwable t) {
//                progressDialog.dismiss();
//                Toast.makeText(KontakDarurat.this, "Data Gagal Ter-input", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

}