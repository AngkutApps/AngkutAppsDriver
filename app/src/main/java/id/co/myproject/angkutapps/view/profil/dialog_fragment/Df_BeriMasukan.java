package id.co.myproject.angkutapps.view.profil.dialog_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import id.co.myproject.angkutapps.R;

public class Df_BeriMasukan extends DialogFragment {

    Button btnKirim;
    EditText etBeriMasukan;

    public Df_BeriMasukan() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frame_beri_masukan, container, false);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etBeriMasukan = view.findViewById(R.id.etBeriMasukan);
        btnKirim = view.findViewById(R.id.btnKirim);
        AndroidNetworking.initialize(getContext());

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimMasukan(etBeriMasukan.getText().toString().trim());
                Df_BeriMasukan.super.onStop();
                Df_BeriMasukan.super.onDestroyView();
            }
        });

    }

    public void kirimMasukan(String masukan){
        AndroidNetworking.post("http://angkutapps.com/angkut_api/insert_masukan_user.php")
                .addBodyParameter("masukan", masukan)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), "Masukan Berhasil Terkirim", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Masukan Gagal Terkirim", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
