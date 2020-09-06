package id.co.myproject.angkutapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.model.crud_table.tb_rw_pembelian_voucher_driver;
import id.co.myproject.angkutapps.model.data_access_object.LoadVoucher;
import id.co.myproject.angkutapps.view.history.dialog_fragment.Df_voucher_pembelian;

public class rw_voucher_pembelian extends RecyclerView.Adapter<rw_voucher_pembelian.ViewHolder> {

    List<LoadVoucher> listVoucher;
    Context context;
    tb_rw_pembelian_voucher_driver crudTable;

    public rw_voucher_pembelian(Context context, List<LoadVoucher> pembelianVoucher) {
        this.context = context;
        this.listVoucher = pembelianVoucher;
    }

    @Override
    public rw_voucher_pembelian.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_hitsory, parent, false);
        crudTable = new tb_rw_pembelian_voucher_driver(context);
        AndroidNetworking.initialize(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull rw_voucher_pembelian.ViewHolder holder, int position) {
        String[] pembelian = String.valueOf(listVoucher.get(position).getTgl_pembelian()).split(" ");
        String tgl_pembelian = pembelian[0];

        String[] masa_berlaku = String.valueOf(listVoucher.get(position).getMasa_berlaku()).split(" ");
        String tgl_masa_berlaku = masa_berlaku[0];

        if (String.valueOf(listVoucher.get(position).getHarga()).length()==5){
            holder.tv_harga.setText("Rp. "+String.valueOf(listVoucher.get(position).getHarga()).substring(0, 2)+"k");
        }else if (String.valueOf(listVoucher.get(position).getHarga()).length()==4){
            holder.tv_harga.setText("Rp. "+String.valueOf(listVoucher.get(position).getHarga()).substring(0, 1)+"k");
        }
        holder.tv_nama_voucher.setText(listVoucher.get(position).getNama_voucher());
        holder.tv_hari.setText(listVoucher.get(position).getHari_pembelian());
        holder.tv_tanggal.setText(tgl_pembelian);
        holder.img_status.setImageResource(R.drawable.services);
        holder.cv_riwayat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                popupmenu(v, listVoucher.get(position).getId_pembelian_voucher(), position);
                return false;
            }
        });
        holder.cv_riwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new Df_voucher_pembelian(listVoucher.get(position), tgl_masa_berlaku, tgl_pembelian));
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupmenu(v, listVoucher.get(position).getId_pembelian_voucher(), position);
//                Toast.makeText(context, ""+listVoucher.get(position).getId_pembelian_voucher(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeItem(int index){
        listVoucher.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return listVoucher.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_harga, tv_nama_voucher, tv_hari, tv_tanggal;
        ImageView btn_delete, img_status;
        CardView cv_riwayat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_harga = itemView.findViewById(R.id.tv_biaya);
            tv_nama_voucher = itemView.findViewById(R.id.tv_tujuan);
            tv_hari = itemView.findViewById(R.id.tv_hari);
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);
            btn_delete = itemView.findViewById(R.id.show_detail);
            cv_riwayat = itemView.findViewById(R.id.cv_riwayat);
            img_status = itemView.findViewById(R.id.img_status);
        }
    }
    private void popupmenu(View v, int getId, int position){
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.inflate(R.menu.popup_menu);
//        id = getId;
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                crudTable.deleteRiwayatPembelianVoucher(String.valueOf(getId));
                removeItem(position);
//                Toast.makeText(context, ""+getId, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        popupMenu.show();
    }

    private void setFragment(DialogFragment fragment){
        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("dialog");
        if (prev !=null){
            fragmentTransaction.remove(prev);
        }
        fragment.show(fragmentTransaction, "dialog");
    }
}
