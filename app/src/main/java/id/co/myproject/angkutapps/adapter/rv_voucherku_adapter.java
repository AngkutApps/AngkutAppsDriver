package id.co.myproject.angkutapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.model.data_access_object.LoadVoucher;

public class rv_voucherku_adapter extends RecyclerView.Adapter<rv_voucherku_adapter.ViewHolder> {

    Context context;
    List<LoadVoucher> loadVouchers;

    public rv_voucherku_adapter(Context context, List<LoadVoucher> loadBeliVoucher) {
        this.context = context;
        this.loadVouchers = loadBeliVoucher;
    }

    @Override
    public rv_voucherku_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_voucherku, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull rv_voucherku_adapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return loadVouchers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
