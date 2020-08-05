package id.co.myproject.angkutapps;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.Driver;
import id.co.myproject.angkutapps.request.ApiRequest;
import id.co.myproject.angkutapps.request.RetrofitRequest;
import id.co.myproject.angkutapps.view.profil.AkunInfoFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

//    ImageView ivUser;
//    TextView tvUser, tvEditProfil, tvAkunInfo, tvSaldo;
//    SharedPreferences sharedPreferences;
//    ApiRequest apiRequest;
//    int idUser;

    CardView cv_Hadiah, cv_pusatBantuan, cv_kontakDarurat, cv_pengaturan, cv_bagikanFeedback, cv_beriMasukan, cv_FAQ, cv_logOut;

    public ProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cv_Hadiah = view.findViewById(R.id.cv_hadiah);
        cv_pusatBantuan = view.findViewById(R.id.cv_pusatBantuan);
        cv_kontakDarurat = view.findViewById(R.id.cv_kontakDarurat);
        cv_pengaturan = view.findViewById(R.id.cv_pengaturan);
        cv_bagikanFeedback = view.findViewById(R.id.cv_bagikanFeedback);
        cv_beriMasukan = view.findViewById(R.id.cv_beriMasukan);
        cv_FAQ = view.findViewById(R.id.cv_FAQ);
        cv_logOut = view.findViewById(R.id.cv_logOut);

        cv_Hadiah.setOnClickListener(clickListener);
        cv_pusatBantuan.setOnClickListener(clickListener);
        cv_kontakDarurat.setOnClickListener(clickListener);
        cv_pengaturan.setOnClickListener(clickListener);
        cv_bagikanFeedback.setOnClickListener(clickListener);
        cv_beriMasukan.setOnClickListener(clickListener);
        cv_FAQ.setOnClickListener(clickListener);
        cv_logOut.setOnClickListener(clickListener);
//        sharedPreferences = getActivity().getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
//        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
//        idUser = sharedPreferences.getInt(Utils.KODE_DRIVER_KEY, 0);

//        ivUser = view.findViewById(R.id.iv_user);
//        tvUser = view.findViewById(R.id.tv_user);
//        tvEditProfil = view.findViewById(R.id.tv_edit_profil);
//        tvAkunInfo = view.findViewById(R.id.tv_akun_info);
//        tvSaldo = view.findViewById(R.id.tv_saldo);
//
//        loadData();
//        tvAkunInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setFragment(new AkunInfoFragment());
//            }
//        });



    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cv_hadiah:
                    break;
                case R.id.cv_pusatBantuan:
                    break;
                case R.id.cv_kontakDarurat:
                    break;
                case R.id.cv_pengaturan:
                    break;
                case R.id.cv_bagikanFeedback:
                    break;
                case R.id.cv_beriMasukan:
                    break;
                case R.id.cv_FAQ:
                    break;
                case R.id.cv_logOut:
                    break;
            }
        }
    };

//    private void loadData() {
//        Call<Driver> callUser = apiRequest.userByIdRequest(idUser);
//        callUser.enqueue(new Callback<Driver>() {
//            @Override
//            public void onResponse(Call<Driver> call, Response<Driver> response) {
//                if (response.isSuccessful()){
//                    Driver user = response.body();
//                    Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR+"profil/"+user.getFoto()).into(ivUser);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Driver> call, Throwable t) {
//                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void setFragment(Fragment fragment){
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame_home, fragment);
//        fragmentTransaction.commit();
//    }
}
