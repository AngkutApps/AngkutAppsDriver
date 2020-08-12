package id.co.myproject.angkutapps.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.model.data_access_object.loadView_rw_voucher_penggunaan;

public class rw_voucher_penggunaan extends RecyclerView.Adapter<rw_voucher_penggunaan.ViewHolder> {

    Context context;
    List<loadView_rw_voucher_penggunaan> listPenggunaan;

    public rw_voucher_penggunaan(Context context, List<loadView_rw_voucher_penggunaan> penggunaanVoucher) {
        this.context = context;
        this.listPenggunaan = penggunaanVoucher;
    }

    @Override
    public rw_voucher_penggunaan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_voucherku, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull rw_voucher_penggunaan.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listPenggunaan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
