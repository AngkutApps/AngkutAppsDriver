package id.co.myproject.angkutapps.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.List;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.model.crud_table.tb_rw_pembelian_voucher_driver;
import id.co.myproject.angkutapps.model.crud_table.tb_rw_penggunaan_voucher_driver;
import id.co.myproject.angkutapps.model.data_access_object.loadView_rw_voucher_penggunaan;
import id.co.myproject.angkutapps.view.history.dialog_fragment.Df_voucher_penggunaan;

public class rw_voucher_penggunaan extends RecyclerView.Adapter<rw_voucher_penggunaan.ViewHolder> {

    Context context;
    List<loadView_rw_voucher_penggunaan> listPenggunaan;
    tb_rw_penggunaan_voucher_driver crudTable;

    public rw_voucher_penggunaan(Context context, List<loadView_rw_voucher_penggunaan> penggunaanVoucher) {
        this.context = context;
        this.listPenggunaan = penggunaanVoucher;
    }

    @Override
    public rw_voucher_penggunaan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_voucherku, parent, false);
        crudTable = new tb_rw_penggunaan_voucher_driver(context);
        AndroidNetworking.initialize(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull rw_voucher_penggunaan.ViewHolder holder, int position) {
        String[] tglPenggunaan = String.valueOf(listPenggunaan.get(position).getTgl_penggunaan()).split(" ");
        String tgl_penggunaan = tglPenggunaan[0];

        holder.tv_title.setText(listPenggunaan.get(position).getNama_voucher());
        holder.tv_hari.setText(listPenggunaan.get(position).getHari_penggunaan());
        holder.tv_tanggal.setText(tgl_penggunaan);
        holder.cv_voucher_penggunaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new Df_voucher_penggunaan(listPenggunaan.get(position)));
            }
        });
        holder.cv_voucher_penggunaan.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                popupmenu(v, listPenggunaan.get(position).getId_penggunaan_voucher());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPenggunaan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_hari, tv_tanggal;
        CardView cv_voucher_penggunaan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_tujuan);
            tv_hari = itemView.findViewById(R.id.tv_tanggal);
            tv_tanggal = itemView.findViewById(R.id.tv_sdank);
            cv_voucher_penggunaan = itemView.findViewById(R.id.cv_penggunaan);

        }
    }

    private void popupmenu(View v, int getId){
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.inflate(R.menu.popup_menu);
//        id = getId;
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                crudTable.deleteRiwayatPenggunaanVoucher(String.valueOf(getId));
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
