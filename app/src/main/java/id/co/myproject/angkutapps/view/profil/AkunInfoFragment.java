package id.co.myproject.angkutapps.view.profil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import id.co.myproject.angkutapps.BuildConfig;
import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.Driver;
import id.co.myproject.angkutapps.request.ApiRequest;
import id.co.myproject.angkutapps.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AkunInfoFragment extends Fragment {

    ImageView ivBack, ivUser;
    TextView tvUser, tvEmail, tvNama, tvAlamat, tvNoHp, tvKtp, tvMerkMobil, tvPlat, tvJk;
    SharedPreferences sharedPreferences;
    ApiRequest apiRequest;
    int idUser;


    public AkunInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_akun_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        sharedPreferences = getActivity().getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        idUser = sharedPreferences.getInt(Utils.KODE_DRIVER_KEY, 0);
        
        ivBack = view.findViewById(R.id.iv_back);
        ivUser = view.findViewById(R.id.iv_user);
        tvUser = view.findViewById(R.id.tv_user);
        tvEmail = view.findViewById(R.id.tv_email);
        tvNama = view.findViewById(R.id.tv_nama);
        tvAlamat = view.findViewById(R.id.tv_alamat);
        tvNoHp = view.findViewById(R.id.tv_no_hp);
        tvKtp = view.findViewById(R.id.tv_no_ktp);
        tvMerkMobil = view.findViewById(R.id.tv_merk_mobil);
        tvPlat = view.findViewById(R.id.tv_plat);
        tvJk = view.findViewById(R.id.tv_jk);
        
        loadData();
        
    }

    private void loadData() {
        Call<Driver> callUser = apiRequest.userByIdRequest(idUser);
        callUser.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (response.isSuccessful()){
                    Driver user = response.body();
                    tvUser.setText(user.getNama());
                    tvEmail.setText(user.getEmail());
                    tvNama.setText(user.getNama());
//                    tvKtp.setText(user.getKtp());
                    tvMerkMobil.setText(user.getMerkMobil());
                    tvPlat.setText(user.getPlat());
                    tvNoHp.setText(user.getNoHp());
                    tvAlamat.setText(user.getAlamat());
                    Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR+"profil/"+user.getFoto()).into(ivUser);
                }
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
