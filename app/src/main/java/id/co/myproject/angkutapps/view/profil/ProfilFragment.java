package id.co.myproject.angkutapps.view.profil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import de.hdodenhof.circleimageview.CircleImageView;
import id.co.myproject.angkutapps.BuildConfig;
import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.data_access_object.DataDriver;
import id.co.myproject.angkutapps.model.data_access_object.Value;
import id.co.myproject.angkutapps.request.ApiDataDriver;
import id.co.myproject.angkutapps.request.ApiRequest;
import id.co.myproject.angkutapps.request.RetrofitRequest;
import id.co.myproject.angkutapps.view.login.LoginActivity;
import id.co.myproject.angkutapps.view.profil.dialog_fragment.Df_BagikanFeedback;
import id.co.myproject.angkutapps.view.profil.dialog_fragment.Df_BeriMasukan;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import static id.co.myproject.angkutapps.helper.Utils.NOHP_DRIVER_KEY;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

//    ImageView ivUser;
//    TextView tvUser, tvEditProfil, tvAkunInfo, tvSaldo;
//    SharedPreferences sharedPreferences;
//    ApiRequest apiRequest;
//    int idUser;
    CircleImageView img_profil;
    CardView cv_Hadiah, cv_pusatBantuan, cv_kontakDarurat, cv_pengaturan, cv_bagikanFeedback, cv_beriMasukan, cv_FAQ, cv_logOut;
    TextView tv_nama_driver;

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ApiDataDriver apiRequest;
    List<DataDriver> list = new ArrayList<>();

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

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading.....");
        progressDialog.show();
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiDataDriver.class);

        cv_Hadiah = view.findViewById(R.id.cv_hadiah);
        cv_pusatBantuan = view.findViewById(R.id.cv_pusatBantuan);
        cv_kontakDarurat = view.findViewById(R.id.cv_kontakDarurat);
        cv_pengaturan = view.findViewById(R.id.cv_pengaturan);
        cv_bagikanFeedback = view.findViewById(R.id.cv_bagikanFeedback);
        cv_beriMasukan = view.findViewById(R.id.cv_beriMasukan);
        cv_FAQ = view.findViewById(R.id.cv_FAQ);
        cv_logOut = view.findViewById(R.id.cv_logOut);
        img_profil = view.findViewById(R.id.img_profil);
        tv_nama_driver = view.findViewById(R.id.tv_nama_driver);

        sharedPreferences = getContext().getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        loadData();

        cv_Hadiah.setOnClickListener(clickListener);
        cv_pusatBantuan.setOnClickListener(clickListener);
        cv_kontakDarurat.setOnClickListener(clickListener);
        cv_pengaturan.setOnClickListener(clickListener);
        cv_bagikanFeedback.setOnClickListener(clickListener);
        cv_beriMasukan.setOnClickListener(clickListener);
        cv_FAQ.setOnClickListener(clickListener);
        cv_logOut.setOnClickListener(clickListener);
        img_profil.setOnClickListener(clickListener);

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
                case R.id.img_profil:
                    startActivity(new Intent(getActivity(), ProfilUser.class));
                    break;
                case R.id.cv_hadiah:
                    break;
                case R.id.cv_pusatBantuan:
                    break;
                case R.id.cv_kontakDarurat:
                    startActivity(new Intent(getActivity(), KontakDarurat.class));
                    break;
                case R.id.cv_pengaturan:
                    break;
                case R.id.cv_bagikanFeedback:
                    BottomDialogFragment(new Df_BagikanFeedback());
                    break;
                case R.id.cv_beriMasukan:
                    setFragment(new Df_BeriMasukan());
                    break;
                case R.id.cv_FAQ:
                    break;
                case R.id.cv_logOut:
//                    logOutProses();
                    break;
            }
        }
    };

    private void setFragment(DialogFragment fragment){
        FragmentManager fragmentManager = ((FragmentActivity)getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("dialog");
        if (prev !=null){
            fragmentTransaction.remove(prev);
        }
        fragment.show(fragmentTransaction, "dialog");
    }

    private void BottomDialogFragment(BottomSheetDialogFragment frag){
        FragmentManager fragmentManager = ((FragmentActivity)getContext()).getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        frag.show(fragmentManager, "ExampleBottomSheet");
    }

    private void loadData(){
        String noHpUser = sharedPreferences.getString(Utils.NOHP_DRIVER_KEY, "");
        Call<List<DataDriver>> userCall = apiRequest.getDataDriver(noHpUser);
        userCall.enqueue(new Callback<List<DataDriver>>() {
            @Override
            public void onResponse(Call<List<DataDriver>> call, Response<List<DataDriver>> response) {
                if (response.isSuccessful()){
                    list = response.body();
                    int fotoUser;
                    if(list.get(0).getFoto().equals("")){
                        if (String.valueOf(list.get(0).getJenis_kelamin()).equals("L")){
                            fotoUser = R.drawable.person_male;
                        }else {
                            fotoUser = R.drawable.person_female;
                        }
                        Glide.with(getActivity()).load(fotoUser)
                                .into(img_profil);
                    }else {
                        Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR+"profil/"+list.get(0).getFoto())
                                .into(img_profil);
                    }
                    tv_nama_driver.setText(list.get(0).getNama_driver());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<DataDriver>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void logOutProses() {
//        String noHpUser = sharedPreferences.getString(Utils.NOHP_DRIVER_KEY, "");
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Log Out");
//        builder.setMessage("Apakah anda yakin ingin Log Out ?");
//        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                progressDialog.show();
//                Call<Value> signOut = apiRequest.logoutDriverRequest(noHpUser);
//                signOut.enqueue(new Callback<Value>() {
//                    @Override
//                    public void onResponse(Call<Value> call, Response<Value> response) {
//                        if (response.isSuccessful()){
//                            if (response.body().getValue() == 1){
//                                editor.putString(Utils.KODE_DRIVER_KEY, "");
//                                editor.putBoolean(Utils.LOGIN_STATUS, false);
//                                editor.commit();
//                                progressDialog.dismiss();
//                                dialog.dismiss();
//                                Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getContext(), LoginActivity.class);
//                                getContext().startActivity(intent);
//                                ((Activity) getContext()).finish();
//                            }else {
//                                progressDialog.dismiss();
//                                dialog.dismiss();
//                                Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Value> call, Throwable t) {
//                        progressDialog.dismiss();
//                        dialog.dismiss();
//                        Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }

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
