package id.co.myproject.angkutapps.vuew.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import id.co.myproject.angkutapps.model.Value;
import id.co.myproject.angkutapps.request.ApiRequest;
import id.co.myproject.angkutapps.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.angkutapps.helper.Utils.ID_USER_KEY;
import static id.co.myproject.angkutapps.helper.Utils.LOGIN_KEY;
import static id.co.myproject.angkutapps.helper.Utils.LOGIN_STATUS;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    EditText etEmail, etPassword;
    Button btnSignIn;
    TextView tvRegistrasi, tvLupaPassword, tv_email;
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

        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnSignIn = view.findViewById(R.id.btn_sign_in);
        tvRegistrasi = view.findViewById(R.id.tv_registrasi);
        tvLupaPassword = view.findViewById(R.id.tv_lupa_password);
        tv_email = view.findViewById(R.id.tv_email);
        parentFrameLayout = getActivity().findViewById(R.id.frame_login);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Memproses ...");
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences(LOGIN_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        btnSignIn.setEnabled(false);
        btnSignIn.setTextColor(Color.argb(50,255,255, 255));

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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

        tvLupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new ForgotPassFragment());
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void cekForm(){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (etEmail.getText().toString().matches(emailPattern)) {
            if (etPassword.length() >= 8) {
                progressDialog.show();
                btnSignIn.setEnabled(true);
                prosesLogin(email, password);
            } else {
                Toast.makeText(getActivity(), "Password kurang boss", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Username atau Password salah boss", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkInput() {
        if (!TextUtils.isEmpty(etEmail.getText())){
            if (!TextUtils.isEmpty(etPassword.getText())){
                btnSignIn.setEnabled(true);
                btnSignIn.setTextColor(Color.rgb(255,255, 255));
            }else {
                btnSignIn.setEnabled(false);
                btnSignIn.setTextColor(Color.argb(50,255,255, 255));
            }
        }else {
            btnSignIn.setEnabled(false);
            btnSignIn.setTextColor(Color.argb(50,255,255, 255));
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

    private void prosesLogin(final String email, final String password) {
        Call<Value> callLoginUser = apiRequest.loginUserRequest(email, password);
        callLoginUser.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()) {
                    if (response.body().getValue() == 1) {
                        idUser = response.body().getIdUser();
                        Call<Value> loginUser = apiRequest.loginUserRequest(email, password);
                        loginUser.enqueue(new Callback<Value>() {
                            @Override
                            public void onResponse(Call<Value> call, Response<Value> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    if(response.body().getValue() == 1){
                                        progressDialog.dismiss();
                                        editor.putInt(ID_USER_KEY, idUser);
                                        editor.putBoolean(LOGIN_STATUS, true);
                                        editor.commit();
                                        updateUI();
                                    }else {
                                        progressDialog.dismiss();
                                        btnSignIn.setEnabled(true);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Value> call, Throwable t) {
                                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {

            }
        });
    }

}
