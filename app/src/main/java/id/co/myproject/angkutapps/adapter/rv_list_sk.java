package id.co.myproject.angkutapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.model.data_access_object.LoadSKVoucher;

public class rv_list_sk extends RecyclerView.Adapter<rv_list_sk.ViewHolder> {

    private Context context;
    private List<LoadSKVoucher> listSK;

    public rv_list_sk(Context context, List<LoadSKVoucher> listSK) {
        this.context = context;
        this.listSK = listSK;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_list_sk, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.numberSK.setText((position+1)+".");
        holder.teksSK.setText(listSK.get(position).getDeskripsi_voucher());
    }

    @Override
    public int getItemCount() {
        return listSK.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView teksSK, numberSK;

        public ViewHolder(View itemView) {
            super(itemView);

            teksSK = itemView.findViewById(R.id.teksSK);
            numberSK = itemView.findViewById(R.id.numberSK);

        }
    }
}
