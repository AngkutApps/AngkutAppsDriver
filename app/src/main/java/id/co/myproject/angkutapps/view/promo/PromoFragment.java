package id.co.myproject.angkutapps.view.promo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.adapter.rw_voucher_pembelian;
import id.co.myproject.angkutapps.adapter.*;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.data_access_object.LoadVoucher;
import id.co.myproject.angkutapps.request.ApiRequestPromo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromoFragment extends Fragment {

    TextView btnVoucherku, btnBeliVoucherku;
    RecyclerView rvPromo;

    rv_beli_voucher_adapter beliVoucherAdapter;
    rv_voucherku_adapter voucherkuAdapter;

    rw_voucher_pembelian rwvoucherpembelian;
    SharedPreferences sharedPreferences;

    ProgressDialog progressDialog;
    List<LoadVoucher> loadBeliVoucher = new ArrayList<>();

    public PromoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promo, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Mohon Tunggu.......");
        sharedPreferences = getActivity().getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);

        btnVoucherku = view.findViewById(R.id.btnVoucherku);
        btnBeliVoucherku = view.findViewById(R.id.btnBeliVoucher);

        btnVoucherku.setOnClickListener(clickListener);
        btnBeliVoucherku.setOnClickListener(clickListener);

        rvPromo = view.findViewById(R.id.rv_promo);
        rvPromo.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPromo.setHasFixedSize(true);

        defaultViewButton();

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnVoucherku:
                    viewButtonNormal();
                    defaultViewButton();
                    break;
                case R.id.btnBeliVoucher:
                    viewButtonNormal();
                    btnBeliVoucherku.setBackgroundColor(Color.parseColor("#008577"));
                    btnBeliVoucherku.setTextColor(Color.parseColor("#FFFFFF"));
                    progressDialog.show();
                    loadBagianPromo(2);
                    break;
            }
        }
    };

    private void viewButtonNormal() {
        btnBeliVoucherku.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btnBeliVoucherku.setTextColor(Color.parseColor("#008577"));
        btnVoucherku.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btnVoucherku.setTextColor(Color.parseColor("#008577"));
    }

    private void defaultViewButton() {
        btnVoucherku.setBackgroundColor(Color.parseColor("#008577"));
        btnVoucherku.setTextColor(Color.parseColor("#FFFFFF"));
        progressDialog.show();
        loadBagianPromo(3);
//        setFragment(new PerjalananFragment());
    }

    public void loadBagianPromo(int i) {
        String noHpUser = sharedPreferences.getString(Utils.NOHP_DRIVER_KEY, "");

        Call<List<LoadVoucher>> call = null;
        if (i==2){
            call = ApiRequestPromo.getInstance().getApi().getPromoBeliVoucher();
        }else if (i==3){
            call = ApiRequestPromo.getInstance().getApi().getPromoVoucherku(noHpUser);
        }
        call.enqueue(new Callback<List<LoadVoucher>>() {
            @Override
            public void onResponse(Call<List<LoadVoucher>> call, Response<List<LoadVoucher>> response) {
                loadBeliVoucher = response.body();

                if (i==2){
                    beliVoucherAdapter = new rv_beli_voucher_adapter(getContext(), loadBeliVoucher);
                    rvPromo.setAdapter(beliVoucherAdapter);
                }else if (i==3){
                    voucherkuAdapter = new rv_voucherku_adapter(getContext(), loadBeliVoucher);
//                    Log.i("Adakahh")
                    rvPromo.setAdapter(voucherkuAdapter);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<LoadVoucher>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}