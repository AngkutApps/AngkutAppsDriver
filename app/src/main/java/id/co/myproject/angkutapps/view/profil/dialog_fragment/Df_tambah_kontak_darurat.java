package id.co.myproject.angkutapps.view.profil.dialog_fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.androidnetworking.AndroidNetworking;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.crud_table.tb_kontak_darurat_user;
import id.co.myproject.angkutapps.view.profil.KontakDarurat;

public class Df_tambah_kontak_darurat extends DialogFragment {

    tb_kontak_darurat_user crudKontakDarurat;
    SharedPreferences sharedPreferences;

    Button btnSaveKontak;
    EditText etNamaKontakDarurat, etHubunganKontak, et_nomor_hp;

    int kondisi = 0;

    public Df_tambah_kontak_darurat() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.df_tambah_kontak,  container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNamaKontakDarurat = view.findViewById(R.id.etNamaKontakDarurat);
        etHubunganKontak = view.findViewById(R.id.etHubunganKontak);
        et_nomor_hp = view.findViewById(R.id.et_nomor_hp);
        btnSaveKontak = view.findViewById(R.id.btnSaveKontak);

        AndroidNetworking.initialize(getContext());
        sharedPreferences = getContext().getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        String noHpUser = sharedPreferences.getString(Utils.NOHP_DRIVER_KEY, "");
        crudKontakDarurat = new tb_kontak_darurat_user(getContext(), noHpUser);

        try {
            if (getArguments()!=null){
                etNamaKontakDarurat.setText(getArguments().getString("namaKontak"));
                etHubunganKontak.setText(getArguments().getString("hubunganKontak"));
                et_nomor_hp.setText(getArguments().getString("nomorKontak"));
                et_nomor_hp.setKeyListener(null);
                kondisi = 2;
            }else {
                kondisi = 1;
            }
        }catch (Exception ex){
            Toast.makeText(getContext(), ""+ex, Toast.LENGTH_SHORT).show();
        }

        btnSaveKontak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kondisi==1) {
                    if (etNamaKontakDarurat.getText().toString().trim() == null) {
                        etNamaKontakDarurat.setError("Kosong");
                    } else if (etHubunganKontak.getText().toString().trim().equals("")) {
                        etHubunganKontak.setError("Kosong");
                    } else if (et_nomor_hp.getText().toString().trim() == null) {
                        et_nomor_hp.setError("Kosong");
                    } else {
                        crudKontakDarurat.tambahKontakDarurat(etNamaKontakDarurat.getText().toString().trim(), etHubunganKontak.getText().toString().trim(), et_nomor_hp.getText().toString().trim());
                    }
                }else if (kondisi==2){
                    if(etNamaKontakDarurat.getText().toString().trim().equals("")){
                        etNamaKontakDarurat.setError("Kosong");
                    }else if (etHubunganKontak.getText().toString().trim().equals("")){
                        etHubunganKontak.setError("Kosong");
                    }else {
                        crudKontakDarurat.updateKontakDarurat(etNamaKontakDarurat.getText().toString().trim(), etHubunganKontak.getText().toString().trim(),
                                et_nomor_hp.getText().toString().trim());
                    }
                }
                Df_tambah_kontak_darurat.super.onStop();
                Df_tambah_kontak_darurat.super.onDestroyView();
            }
        });



    }
}
