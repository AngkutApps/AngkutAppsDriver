package id.co.myproject.angkutapps.view.history;

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
import id.co.myproject.angkutapps.adapter.rv_voucherku_adapter;
import id.co.myproject.angkutapps.adapter.rw_voucher_pembelian;
import id.co.myproject.angkutapps.adapter.rw_voucher_penggunaan;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.data_access_object.LoadVoucher;
import id.co.myproject.angkutapps.model.data_access_object.loadView_rw_voucher_penggunaan;
import id.co.myproject.angkutapps.request.ApiRequestRiwayat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoucherFragment extends Fragment {

    rw_voucher_pembelian rwvoucherpembelian;
    rw_voucher_penggunaan rwvoucherpenggunaan;

    RecyclerView rvHistory;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    TextView btnPenggunaanVoucher, btnPembelianVoucher;

    List<loadView_rw_voucher_penggunaan> PenggunaanVoucher = new ArrayList<>();
    List<LoadVoucher> PembelianVoucher = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voucher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Tunggu Sebentar....");

        sharedPreferences = getActivity().getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);

        btnPenggunaanVoucher = view.findViewById(R.id.btnPenggunaanVoucher);
        btnPembelianVoucher = view.findViewById(R.id.btnPembelianVoucher);

        rvHistory = view.findViewById(R.id.rv_voucher);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistory.setHasFixedSize(true);

        btnPembelianVoucher.setOnClickListener(clickListener);
        btnPenggunaanVoucher.setOnClickListener(clickListener);

        defaultViewButton();

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnPenggunaanVoucher :
                    viewButtonNormal();
                    defaultViewButton();
                    break;
                case R.id.btnPembelianVoucher :
                    viewButtonNormal();
                    btnPembelianVoucher.setBackgroundResource(R.drawable.bg_button_voucher);
                    btnPembelianVoucher.setTextColor(Color.parseColor("#FFFFFF"));
                    progressDialog.show();
                    loadPembelianVoucher();
                    break;
            }
        }
    };

    private void viewButtonNormal(){
        btnPembelianVoucher.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btnPembelianVoucher.setTextColor(Color.parseColor("#008577"));
        btnPenggunaanVoucher.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btnPenggunaanVoucher.setTextColor(Color.parseColor("#008577"));
    }

    private void defaultViewButton(){
        btnPenggunaanVoucher.setBackgroundResource(R.drawable.bg_button_voucher);
        btnPenggunaanVoucher.setTextColor(Color.parseColor("#FFFFFF"));
        progressDialog.show();
        loadPenggunaanVoucher();
    }

    public void loadPenggunaanVoucher() {
        String noHpUser = sharedPreferences.getString(Utils.NOHP_DRIVER_KEY, "");

        Call<List<loadView_rw_voucher_penggunaan>> call = ApiRequestRiwayat.getInstance().getApi().getRiwayatPenggunaanVoucher(noHpUser);

        call.enqueue(new Callback<List<loadView_rw_voucher_penggunaan>>() {
            @Override
            public void onResponse(Call<List<loadView_rw_voucher_penggunaan>> call, Response<List<loadView_rw_voucher_penggunaan>> response) {
                PenggunaanVoucher = response.body();

                rwvoucherpenggunaan = new rw_voucher_penggunaan(getContext(), PenggunaanVoucher);
                rvHistory.setAdapter(rwvoucherpenggunaan);

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<loadView_rw_voucher_penggunaan>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    public void loadPembelianVoucher() {
        String noHpUser = sharedPreferences.getString(Utils.NOHP_DRIVER_KEY, "");

        Call<List<LoadVoucher>> call = ApiRequestRiwayat.getInstance().getApi().getRiwayatPembelianVoucher(noHpUser);

        call.enqueue(new Callback<List<LoadVoucher>>() {
            @Override
            public void onResponse(Call<List<LoadVoucher>> call, Response<List<LoadVoucher>> response) {
                PembelianVoucher = response.body();
//                Log.i("Hasil", ""+response.body());

                rwvoucherpembelian = new rw_voucher_pembelian(getContext(), PembelianVoucher);
                rvHistory.setAdapter(rwvoucherpembelian);

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<LoadVoucher>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}