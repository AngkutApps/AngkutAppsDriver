package id.co.myproject.angkutapps.view.profil.dialog_fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

        btnSaveKontak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crudKontakDarurat.tambahKontakDarurat(etNamaKontakDarurat.getText().toString().trim(), etHubunganKontak.getText().toString().trim(), et_nomor_hp.getText().toString().trim());
                Df_tambah_kontak_darurat.super.onStop();
                Df_tambah_kontak_darurat.super.onDestroyView();
            }
        });



    }
}
