package id.co.myproject.angkutapps.view.history;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.adapter.RiwayatAdapter;

public class VoucherFragment extends Fragment {

    RiwayatAdapter riwayatAdapter;
    RecyclerView rvHistory;

    TextView btnPenggunaanVoucher, btnPembelianVoucher;

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

        btnPenggunaanVoucher = view.findViewById(R.id.btnPenggunaanVoucher);
        btnPembelianVoucher = view.findViewById(R.id.btnPembelianVoucher);

        rvHistory = view.findViewById(R.id.rv_voucher);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistory.setHasFixedSize(true);
        riwayatAdapter = new RiwayatAdapter(getContext());
        rvHistory.setAdapter(riwayatAdapter);

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
    }
}