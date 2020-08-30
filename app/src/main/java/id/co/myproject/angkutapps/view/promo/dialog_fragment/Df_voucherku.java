package id.co.myproject.angkutapps.view.promo.dialog_fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.adapter.rv_list_sk;
import id.co.myproject.angkutapps.model.data_access_object.LoadVoucher;

public class Df_voucherku extends DialogFragment {

    ImageView imgClose;
    TextView titleVoucher, masaBerlakuVoucher, deskripsiVoucher;
    RecyclerView rvSyaratKetentuan;

    rv_list_sk list_sk;

    ProgressDialog progressDialog;

    String masa_berlaku;

    LoadVoucher loadVoucher;

    public Df_voucherku(LoadVoucher loadVoucher, String masa_berlaku) {
        this.loadVoucher = loadVoucher;
        this.masa_berlaku = masa_berlaku;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.df_promo_voucherku, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleVoucher = view.findViewById(R.id.titleVoucher);
        masaBerlakuVoucher = view.findViewById(R.id.masaBerlakuVoucher);
        deskripsiVoucher = view.findViewById(R.id.deksripsiVoucher);
        rvSyaratKetentuan = view.findViewById(R.id.rvSyaratKetentuan);
        imgClose = view.findViewById(R.id.imgClose);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Mohon Tunggu....");

        titleVoucher.setText(loadVoucher.getNama_voucher());
        masaBerlakuVoucher.setText(masa_berlaku);
        deskripsiVoucher.setText(loadVoucher.getDeskripsi());

        rvSyaratKetentuan.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSyaratKetentuan.setHasFixedSize(true);
        progressDialog.show();

        list_sk = new rv_list_sk(getContext(), loadVoucher.getSyarat_ketentuan());
        rvSyaratKetentuan.setAdapter(list_sk);
        progressDialog.dismiss();

        imgClose.setOnClickListener(clickListener);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imgClose :
                    Df_voucherku.super.onStop();
                    Df_voucherku.super.onDestroyView();
                    break;
            }
        }
    };

}
