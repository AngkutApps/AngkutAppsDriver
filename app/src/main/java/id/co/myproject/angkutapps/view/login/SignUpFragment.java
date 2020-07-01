package id.co.myproject.angkutapps.view.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.Driver;
import id.co.myproject.angkutapps.model.JenisKendaraan;
import id.co.myproject.angkutapps.model.Value;
import id.co.myproject.angkutapps.request.ApiRequest;
import id.co.myproject.angkutapps.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.angkutapps.helper.Utils.LOGIN_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment{

    ScrollView svSignUp;
    EditText etNama, etEmail, etNomorHp, etMerkMobil, etPlatMobil;
    Spinner spJenisKendaraan;
    RadioButton rbL, rbP;
    Button btnSignUp;
    TextView tv_login;
    private FrameLayout parentFrameLayout;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private ProgressDialog progressDialog;
    int spinnerPosition;
    public static final String TAG = SignUpFragment.class.getSimpleName();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ApiRequest apiRequest;


    private int RESOLVE_HINT = 2;

    List<String> listIdJenis = new ArrayList<>();

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences(LOGIN_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");

        rbL.setChecked(true);

        loadJenisKendaraan();

        spJenisKendaraan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerPosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isConnectionInternet(getActivity())){
                    checkInput();
                }else {
                    Toast.makeText(getActivity(), "Tidak ada jaringan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });


    }

    private void initView(View view) {
        svSignUp = view.findViewById(R.id.sv_sign_up);
        etNama = view.findViewById(R.id.et_nama);
        etEmail = view.findViewById(R.id.et_email);
        rbL = view.findViewById(R.id.rb_l);
        rbP = view.findViewById(R.id.rb_p);
        etNomorHp = view.findViewById(R.id.et_nomor_hp);
        btnSignUp = view.findViewById(R.id.btn_sign_up);
        etMerkMobil = view.findViewById(R.id.et_merk_mobil);
        etPlatMobil = view.findViewById(R.id.etPlatMobil);
        spJenisKendaraan = view.findViewById(R.id.sp_jenis_kendaraan);
        tv_login = view.findViewById(R.id.tv_login);
        parentFrameLayout = getActivity().findViewById(R.id.frame_login);
    }

    private void loadJenisKendaraan() {
        List<String> listJenis = new ArrayList<>();
        Call<List<JenisKendaraan>> callJenisKendaraan = apiRequest.getJenisKendaraan();
        callJenisKendaraan.enqueue(new Callback<List<JenisKendaraan>>() {
            @Override
            public void onResponse(Call<List<JenisKendaraan>> call, Response<List<JenisKendaraan>> response) {
                List<JenisKendaraan> jenisKendaraanList = response.body();
                for (int i=0; i<jenisKendaraanList.size(); i++){
                    listJenis.add(jenisKendaraanList.get(i).getNamaJenisKendaraan());
                    listIdJenis.add(jenisKendaraanList.get(i).getIdJenisKendaraan());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.weekofday, listJenis);
                spJenisKendaraan.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<JenisKendaraan>> call, Throwable t) {

            }
        });
    }
    private void checkInput() {
        progressDialog.show();
        if (!TextUtils.isEmpty(etEmail.getText())) {
            if (etEmail.getText().toString().matches(emailPattern)) {
                if (!TextUtils.isEmpty(etNomorHp.getText())){
                    if (!TextUtils.isEmpty(etNama.getText())) {
                        if (!TextUtils.isEmpty(etMerkMobil.getText())){
                            if (!TextUtils.isEmpty(etPlatMobil.getText())){
                                progressDialog.show();
                                Call<Value> cekNoHPCall = apiRequest.cekNoHpRequest(etNomorHp.getText().toString());
                                cekNoHPCall.enqueue(new Callback<Value>() {
                                    @Override
                                    public void onResponse(Call<Value> call, Response<Value> response) {
                                        if (response.isSuccessful()){
                                            if (response.body().getValue() == 1){
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(), "No Hp sudah digunakan, gunakan no hp yang lain", Toast.LENGTH_SHORT).show();
                                            }else {
                                                daftar();
                                            }
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<Value> call, Throwable t) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                progressDialog.dismiss();
                                etPlatMobil.setError("Plat mobil tidak boleh kosong");
                                Utils.scrollToView(svSignUp, etPlatMobil);
                            }
                        }else {
                            progressDialog.dismiss();
                            etMerkMobil.setError("Merk mobil tidak boleh kosong");
                            Utils.scrollToView(svSignUp, etMerkMobil);
                        }
                    } else {
                        progressDialog.dismiss();
                        etNama.setError("Nama tidak boleh kosong");
                        Utils.scrollToView(svSignUp, etNama);
                    }
                }else {
                    progressDialog.dismiss();
                    etNama.setError("No Hp tidak boleh kosong");
                    Utils.scrollToView(svSignUp, etNomorHp);
                }
            }else {
                etEmail.setError("Email tidak sesuai format");
                Utils.scrollToView(svSignUp, etEmail);
            }
        }else {
            etEmail.setError("Email tidak boleh kosong");
            Utils.scrollToView(svSignUp, etEmail);
        }
    }


    private void daftar(){
        final String nama = etNama.getText().toString();
        final String email = etEmail.getText().toString();
        final String noHp = etNomorHp.getText().toString();
        final String merkMobil = etMerkMobil.getText().toString();
        final String platMobil = etPlatMobil.getText().toString();
        Driver user = new Driver();
        String id = UUID.randomUUID().toString();
        user.setKodeDriver(id.substring(0, 10));
        user.setEmail(email);
        user.setNama(nama);
        user.setNoHp(noHp);

        if (rbP.isChecked()){
            user.setJk("P");
        }else if (rbL.isChecked()){
            user.setJk("L");
        }

        user.setMerkMobil(merkMobil);
        user.setPlat(platMobil);
        user.setIdJenisKendaraan(listIdJenis.get(spinnerPosition));

        Bundle bundle = new Bundle();
        bundle.putParcelable("user_data", user);
        bundle.putInt("type_sign", Utils.TYPE_SIGN_UP_BUNDLE);
        KonfirmasiEmailFragment konfirmasiEmailFragment = new KonfirmasiEmailFragment();
        konfirmasiEmailFragment.setArguments(bundle);

        progressDialog.dismiss();
        setFragment(konfirmasiEmailFragment);

//        if (etEmail.getText().toString().matches(emailPattern)){
//            if (etPassword.getText().toString().equals(etKonfirm.getText().toString())){
//                progressDialog.show();
//                String uuid = UUID.randomUUID().toString();
//                Call<Value> callRegistrasiUser = apiRequest.registrasiUserRequest(
//                        uuid.substring(0, 10),
//                        email,
//                        password,
//                        nama,
//                        ""
//                );
//                callRegistrasiUser.enqueue(new Callback<Value>() {
//                    @Override
//                    public void onResponse(Call<Value> call, Response<Value> response) {
//                        if (response.body().getValue() == 1){
//                            idUser = response.body().getIdUser();
//                            editor.putInt(ID_USER_KEY, idUser);
//                            editor.putBoolean(LOGIN_STATUS, true);
//                            editor.commit();
//                            Intent intent = new Intent(getActivity(), MainActivity.class);
//                            startActivity(intent);
//                            getActivity().finish();
//                        }else {
//                            progressDialog.dismiss();
//                            btnSignUp.setEnabled(true);
//                            Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Value> call, Throwable t) {
//                        Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }else {
//            etEmail.setError("Email tidak cocok");
//        }
    }


    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }


}
