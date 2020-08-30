package id.co.myproject.angkutapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.util.List;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.model.data_access_object.loadView_rw_perjalanan;
import id.co.myproject.angkutapps.view.history.dialog_fragment.Df_DetailRiwayatFragment;

public class rv_rw_perjalanan extends RecyclerView.Adapter<rv_rw_perjalanan.ViewHolder> {

    private Context context;
    private List<loadView_rw_perjalanan> listPerjalanan;

    public rv_rw_perjalanan(Context context, List<loadView_rw_perjalanan> loadRiwayatPerjalanan) {
        this.context = context;
        this.listPerjalanan = loadRiwayatPerjalanan;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_hitsory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] tglBerangkat = String.valueOf(listPerjalanan.get(position).getTgl_berangkat()).split(" ");
        String tgl_berangkat = tglBerangkat[0];


        holder.tv_tujuan.setText(listPerjalanan.get(position).getDari()+" -> "+listPerjalanan.get(position).getTujuan());
//        holder.tv_hari.setText(listPerjalanan.get(position).getHari_keberangkatan());
        holder.tv_tanggal.setText(tgl_berangkat);
        if (String.valueOf(listPerjalanan.get(position).getBiaya()).length()==7){
            holder.tv_biaya.setText("Rp. "+String.valueOf(listPerjalanan.get(position).getBiaya()).substring(0, 4)+"k");
        }else if (String.valueOf(listPerjalanan.get(position).getBiaya()).length()==6){
            holder.tv_biaya.setText("Rp. "+String.valueOf(listPerjalanan.get(position).getBiaya()).substring(0, 3)+"k");
        }else if (String.valueOf(listPerjalanan.get(position).getBiaya()).length()==5){
            holder.tv_biaya.setText("Rp. "+String.valueOf(listPerjalanan.get(position).getBiaya()).substring(0, 2)+"k");
        }else if (String.valueOf(listPerjalanan.get(position).getBiaya()).length()==4){
            holder.tv_biaya.setText("Rp. "+String.valueOf(listPerjalanan.get(position).getBiaya()).substring(0, 1)+"k");
        }
        holder.cv_riwayat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                popupmenu(v, listPerjalanan.get(position).getId_destinasi());
                return false;
            }
        });
        holder.cv_riwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new Df_DetailRiwayatFragment(listPerjalanan.get(position)));
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupmenu(v, listPerjalanan.get(position).getId_destinasi());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listPerjalanan.size();
    }

    public class ViewHolder  extends  RecyclerView.ViewHolder{

        TextView tv_tujuan, tv_hari, tv_tanggal, tv_biaya;
        ImageView btn_delete;
        CardView cv_riwayat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_tujuan = itemView.findViewById(R.id.tv_tujuan);
            tv_hari = itemView.findViewById(R.id.tv_hari);
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);
            tv_biaya = itemView.findViewById(R.id.tv_biaya);
            btn_delete = itemView.findViewById(R.id.show_detail);
            cv_riwayat = itemView.findViewById(R.id.cv_riwayat);

        }
    }

    private void popupmenu(View v, int getId){
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.inflate(R.menu.popup_menu);
//        id = getId;
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(context, ""+getId, Toast.LENGTH_SHORT).show();
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
