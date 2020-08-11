package id.co.myproject.angkutapps.view.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.angkutapps.MainActivity;
import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.data_access_object.Value;
import id.co.myproject.angkutapps.request.ApiRequest;
import id.co.myproject.angkutapps.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.angkutapps.helper.Utils.LOGIN_KEY;
import static id.co.myproject.angkutapps.helper.Utils.LOGIN_STATUS;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {


    EditText etNoHp;
    Button btnSignIn;
    TextView tvRegistrasi, tv_email;
    FrameLayout parentFrameLayout;
    int idUser, login_level;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    ApiRequest apiRequest;
    private boolean userExists = false;
    ProgressDialog progressDialog;
    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        etNoHp = view.findViewById(R.id.et_nomor_hp);
        btnSignIn = view.findViewById(R.id.btn_sign_in);
        tvRegistrasi = view.findViewById(R.id.tv_registrasi);
        tv_email = view.findViewById(R.id.tv_email);
        parentFrameLayout = getActivity().findViewById(R.id.frame_login);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Memproses ...");
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences(LOGIN_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isConnectionInternet(getActivity())){
                    cekForm();
                }else {
                    Toast.makeText(getActivity(), "Tidak ada jaringan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvRegistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignUpFragment());
            }
        });

    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    private void cekForm(){
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        if (!TextUtils.isEmpty(etNoHp.getText())) {
            Call<Value> cekNoHPCall = apiRequest.cekNoHpRequest(etNoHp.getText().toString());
            cekNoHPCall.enqueue(new Callback<Value>() {
                @Override
                public void onResponse(Call<Value> call, Response<Value> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()){
                        if (response.body().getValue() == 1){
                            Bundle bundle = new Bundle();
                            bundle.putString("no_hp", etNoHp.getText().toString());
                            bundle.putInt("type_sign", Utils.TYPE_SIGN_IN_BUNDLE);
                            KonfirmasiEmailFragment konfirmasiEmailFragment = new KonfirmasiEmailFragment();
                            konfirmasiEmailFragment.setArguments(bundle);
                            setFragment(konfirmasiEmailFragment);
                        }else {
                            Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<Value> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "No HP tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        boolean statusLogin = sharedPreferences.getBoolean(LOGIN_STATUS, false);
        if (statusLogin){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }else {
            progressDialog.dismiss();
            setFragment(new SignInFragment());
        }
    }

}
