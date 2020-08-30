package id.co.myproject.angkutapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.model.data_access_object.LoadVoucher;
import id.co.myproject.angkutapps.view.promo.dialog_fragment.Df_voucherku;

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
        String[] masaBerlaku = String.valueOf(loadVouchers.get(position).getMasa_berlaku()).split(" ");
        String masa_berlaku = masaBerlaku[0];

        holder.tv_title.setText(loadVouchers.get(position).getNama_voucher());
        holder.tv_masa_berlaku.setText("Berlaku s/d "+masa_berlaku);
        holder.cv_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new Df_voucherku(loadVouchers.get(position),masa_berlaku));
            }
        });
    }

    @Override
    public int getItemCount() {
        return loadVouchers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_masa_berlaku;
        CardView cv_list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_tujuan);
            tv_masa_berlaku = itemView.findViewById(R.id.tv_tanggal);
            cv_list = itemView.findViewById(R.id.cv_penggunaan);

        }
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
