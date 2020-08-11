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
import id.co.myproject.angkutapps.view.promo.dialog_fragment.Df_beliVoucher;

public class rv_beli_voucher_adapter extends RecyclerView.Adapter<rv_beli_voucher_adapter.ViewHolder> {

    Context context;
    List<LoadVoucher> loadVouchers;

    public rv_beli_voucher_adapter(Context context, List<LoadVoucher> loadBeliVoucher) {
        this.context = context;
        this.loadVouchers = loadBeliVoucher;
    }

    @Override
    public rv_beli_voucher_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_promo_beli_voucher, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull rv_beli_voucher_adapter.ViewHolder holder, int position) {
        String[] masaBerlaku = String.valueOf(loadVouchers.get(position).getMasa_berlaku()).split(" ");
        String masa_berlaku = masaBerlaku[0];

        holder.tvMasaBerlaku.setText(masa_berlaku);
        holder.tvHarga.setText("Rp. "+loadVouchers.get(position).getHarga());
        holder.cvBeliVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
                setFragment(new Df_beliVoucher(loadVouchers, position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return loadVouchers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMasaBerlaku, tvHarga;
        CardView cvBeliVoucher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvBeliVoucher = itemView.findViewById(R.id.cvBeliVoucher);
            tvMasaBerlaku = itemView.findViewById(R.id.tvMasaBerlaku);
            tvHarga = itemView.findViewById(R.id.tvHarga);

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
