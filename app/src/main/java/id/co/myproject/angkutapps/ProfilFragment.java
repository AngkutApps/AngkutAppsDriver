package id.co.myproject.angkutapps;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.User;
import id.co.myproject.angkutapps.request.ApiRequest;
import id.co.myproject.angkutapps.request.RetrofitRequest;
import id.co.myproject.angkutapps.vuew.profil.AkunInfoFragment;
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

    ImageView ivUser;
    TextView tvUser, tvEditProfil, tvAkunInfo, tvSaldo;
    SharedPreferences sharedPreferences;
    ApiRequest apiRequest;
    int idUser;


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
        sharedPreferences = getActivity().getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        idUser = sharedPreferences.getInt(Utils.ID_USER_KEY, 0);

        ivUser = view.findViewById(R.id.iv_user);
        tvUser = view.findViewById(R.id.tv_user);
        tvEditProfil = view.findViewById(R.id.tv_edit_profil);
        tvAkunInfo = view.findViewById(R.id.tv_akun_info);
        tvSaldo = view.findViewById(R.id.tv_saldo);

        loadData();
        tvAkunInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new AkunInfoFragment());
            }
        });



    }

    private void loadData() {
        Call<User> callUser = apiRequest.userByIdRequest(idUser);
        callUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    User user = response.body();
                    Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR+"profil/"+user.getFoto()).into(ivUser);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_home, fragment);
        fragmentTransaction.commit();
    }
}
