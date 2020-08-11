package id.co.myproject.angkutapps.view.promo.dialog_fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;

import java.util.ArrayList;
import java.util.List;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.adapter.rv_list_sk;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.crud_table.tb_promo;
import id.co.myproject.angkutapps.model.data_access_object.LoadSKVoucher;
import id.co.myproject.angkutapps.model.data_access_object.LoadVoucher;
import id.co.myproject.angkutapps.request.ApiRequestPromo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Df_beliVoucher extends DialogFragment {

    TextView tvHarga, tvTitleVoucher, tvMasaBerlaku, tvPoint, tvDeskripsiPromo;
    RecyclerView rvSyaratdanKetentuan;
    ImageView imgClose;
    Button btnBeliVoucher;

    tb_promo crud_tb_promo;
    rv_list_sk listSk_adapter;
    List<LoadSKVoucher> listSK = new ArrayList<>();
    List<LoadVoucher> loadVouchers;
    int position;
    SharedPreferences sharedPreferences;

    public Df_beliVoucher(List<LoadVoucher> loadVouchers, int position) {
        this.loadVouchers = loadVouchers;
        this.position = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_df_beli_voucher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        AndroidNetworking.initialize(getContext());
        crud_tb_promo = new tb_promo(getContext());

        btnBeliVoucher = view.findViewById(R.id.btnBeliVoucher);
        tvTitleVoucher = view.findViewById(R.id.tvTitleVoucher);
        tvMasaBerlaku = view.findViewById(R.id.tvMasaBerlaku);
        tvHarga = view.findViewById(R.id.tvHarga);
        tvPoint = view.findViewById(R.id.tvPoint);
        tvDeskripsiPromo = view.findViewById(R.id.tvDeskripsiPromo);
        rvSyaratdanKetentuan = view.findViewById(R.id.rvSyaratKetentuan);
        imgClose = view.findViewById(R.id.imgClose);

        tvTitleVoucher.setText(loadVouchers.get(position).getNama_voucher());
        tvMasaBerlaku.setText(loadVouchers.get(position).getMasa_berlaku());
        tvHarga.setText(""+loadVouchers.get(position).getHarga());
        tvPoint.setText(""+loadVouchers.get(position).getPoint());
        tvDeskripsiPromo.setText(loadVouchers.get(position).getDeskripsi());

        rvSyaratdanKetentuan.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSyaratdanKetentuan.setHasFixedSize(true);
        loadSyaratKetentuan();

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Df_beliVoucher.super.onStop();
                Df_beliVoucher.super.onDestroyView();
            }
        });

        btnBeliVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noHpUser = sharedPreferences.getString(Utils.NOHP_DRIVER_KEY, "");
                crud_tb_promo.insertBeliVoucher(loadVouchers.get(position).getKode_voucher(), noHpUser);
//                Toast.makeText(getContext(), ""+noHpUser+" --- "+loadVouchers.get(position).getKode_voucher(), Toast.LENGTH_SHORT).show();
                Df_beliVoucher.super.onStop();
                Df_beliVoucher.super.onDestroyView();
            }
        });

    }

    public void loadSyaratKetentuan() {
        Call<List<LoadSKVoucher>> call = ApiRequestPromo.getInstance().getApi().getSKBeliVoucher(loadVouchers.get(position).getKode_voucher());
        call.enqueue(new Callback<List<LoadSKVoucher>>() {
            @Override
            public void onResponse(Call<List<LoadSKVoucher>> call, Response<List<LoadSKVoucher>> response) {
                listSK = response.body();

                listSk_adapter = new rv_list_sk(getContext(), listSK);
                rvSyaratdanKetentuan.setAdapter(listSk_adapter);
            }

            @Override
            public void onFailure(Call<List<LoadSKVoucher>> call, Throwable t) {

            }
        });
    }
}