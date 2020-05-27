package id.co.myproject.angkutapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.model.Riwayat;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {

    List<Riwayat> riwayatList = new ArrayList<>();
    Context context;

    public RiwayatAdapter(Context context) {
        this.context = context;
    }

    public void setRiwayatList(List<Riwayat> riwayatList){
        this.riwayatList.clear();
        this.riwayatList.addAll(riwayatList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RiwayatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatAdapter.ViewHolder holder, int position) {
        Riwayat riwayat = riwayatList.get(position);
        holder.tvHarga.setText(riwayat.getHarga());
        holder.tvAsal.setText(riwayat.getAsal());
        holder.tvTujuan.setText(riwayat.getTujuan());
        holder.tvHari.setText(riwayat.getHari());
        holder.tvTanggal.setText(riwayat.getTanggal());
    }

    @Override
    public int getItemCount() {
        return riwayatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvHarga, tvAsal, tvTujuan, tvHari, tvTanggal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHarga = itemView.findViewById(R.id.tv_harga);
            tvAsal = itemView.findViewById(R.id.tv_asal);
            tvTujuan = itemView.findViewById(R.id.tv_tujuan);
            tvHari = itemView.findViewById(R.id.tv_hari);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
        }
    }
}
