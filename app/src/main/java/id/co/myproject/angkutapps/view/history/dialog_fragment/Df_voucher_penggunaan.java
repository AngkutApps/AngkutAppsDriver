package id.co.myproject.angkutapps.view.history.dialog_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.List;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.model.data_access_object.loadView_rw_voucher_penggunaan;

public class Df_voucher_penggunaan extends DialogFragment {

    ImageView imgClose;
    TextView tvNamaVoucher, tvTglPemakaian, tvHariPemakaian, tvDeskripsiVoucher;

    loadView_rw_voucher_penggunaan listPenggunaan;

    public Df_voucher_penggunaan(loadView_rw_voucher_penggunaan loadView_rw_voucher_penggunaan) {
        this.listPenggunaan = loadView_rw_voucher_penggunaan;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.df_rw_penggunaan_promo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNamaVoucher = view.findViewById(R.id.tvNamaVoucher);
        tvTglPemakaian = view.findViewById(R.id.tvTglPemakaian);
        tvHariPemakaian = view.findViewById(R.id.tvHariPemakaian);
        tvDeskripsiVoucher = view.findViewById(R.id.tvDeskripsiVoucher);
        imgClose = view.findViewById(R.id.imgClose);

        tvNamaVoucher.setText(listPenggunaan.getNama_voucher());
        tvTglPemakaian.setText(listPenggunaan.getTgl_penggunaan());
        tvHariPemakaian.setText(listPenggunaan.getHari_penggunaan());
        tvDeskripsiVoucher.setText(listPenggunaan.getDeskripsi());

        imgClose.setOnClickListener(clickListener);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgClose:
                    Df_voucher_penggunaan.super.onStop();
                    Df_voucher_penggunaan.super.onDestroyView();
                    break;
            }
        }
    };
}
