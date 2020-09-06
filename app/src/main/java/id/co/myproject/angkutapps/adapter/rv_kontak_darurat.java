package id.co.myproject.angkutapps.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.model.crud_table.tb_kontak_darurat_user;
import id.co.myproject.angkutapps.model.data_access_object.LoadKontakDarurat;
import id.co.myproject.angkutapps.view.profil.KontakDarurat;
import id.co.myproject.angkutapps.view.profil.dialog_fragment.Df_tambah_kontak_darurat;

public class rv_kontak_darurat extends RecyclerView.Adapter<rv_kontak_darurat.ViewHolder> {

    private Context context;
    private List<LoadKontakDarurat> kontakDarurats;
    private String noHpUser;
    tb_kontak_darurat_user tbKontakDaruratUser;

    public rv_kontak_darurat(Context context, List<LoadKontakDarurat> arrayList, String noHpUser) {
        this.context = context;
        this.kontakDarurats = arrayList;
        this.noHpUser = noHpUser;
    }

    @Override
    public rv_kontak_darurat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_kontak_darurat, null);
        tbKontakDaruratUser = new tb_kontak_darurat_user(context, noHpUser);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull rv_kontak_darurat.ViewHolder holder, int position) {
        holder.tvNomorKontakDarurat.setText("+62"+kontakDarurats.get(position).getNomor_kontak_darurat());
        holder.tvNamaKontakDarurat.setText(kontakDarurats.get(position).getNama_kontak());
        holder.tvHubunganKontakDarurat.setText("("+kontakDarurats.get(position).getHubungan_kontak()+")");
        holder.btnSettingKontakDarurat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupmenu(v, kontakDarurats.get(position).getId_kontak_darurat(), position);
            }
        });
        holder.rlKontakDarurat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new Df_tambah_kontak_darurat(), kontakDarurats.get(position).getNama_kontak(), kontakDarurats.get(position).getHubungan_kontak(), kontakDarurats.get(position).getNomor_kontak_darurat());
            }
        });
    }

    private void removeItem(int index){
        kontakDarurats.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return kontakDarurats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNamaKontakDarurat, tvHubunganKontakDarurat, tvNomorKontakDarurat;
        ImageButton btnSettingKontakDarurat;
        RelativeLayout rlKontakDarurat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNamaKontakDarurat = itemView.findViewById(R.id.tvNamaKontakDarurat);
            tvHubunganKontakDarurat = itemView.findViewById(R.id.tvHubunganKontakDarurat);
            tvNomorKontakDarurat = itemView.findViewById(R.id.tvNomorKontakDarurat);
            btnSettingKontakDarurat = itemView.findViewById(R.id.btnSettingKontakDarurat);
            rlKontakDarurat = itemView.findViewById(R.id.rlKontakDarurat);
        }
    }

    private void popupmenu(View v, int id, int position){
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                removeItem(position);
                tbKontakDaruratUser.deleteKontakDarurat(""+id);
                return false;
            }
        });
        popupMenu.show();
    }

    private void setFragment(DialogFragment fragment, String namaKontak, String hubunganKontak, String nomor){
        Bundle data = new Bundle();
        data.putString("namaKontak", namaKontak);
        data.putString("hubunganKontak", hubunganKontak);
        data.putString("nomorKontak", nomor);
        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("dialog");
        fragment.setArguments(data);
        if (prev !=null){
            fragmentTransaction.remove(prev);
        }
        fragment.show(fragmentTransaction, "dialog");
    }
}
