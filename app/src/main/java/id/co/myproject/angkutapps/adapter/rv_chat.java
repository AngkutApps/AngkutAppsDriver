package id.co.myproject.angkutapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.co.myproject.angkutapps.model.data_access_object.Chat;
import id.co.myproject.angkutapps.R;

public class rv_chat extends RecyclerView.Adapter<rv_chat.ViewHolder> {

    ArrayList<Chat> listPesan;
    Context context;

    public rv_chat(ArrayList<Chat> listPesan, Context context) {
        this.listPesan = listPesan;
        this.context = context;
    }

    @Override
    public rv_chat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frame_list_pesan, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull rv_chat.ViewHolder holder, int position) {
        if (listPesan.get(position).getKondisi().equalsIgnoreCase("Driver")) {
            holder.cvPesan.setBackgroundResource(R.color.cardview_dark_background);
        } else {
            holder.cvPesan.setBackgroundResource(R.color.colorGrey);
        }
        holder.tv_tanggal.setText(listPesan.get(position).getWaktu());
        holder.tv_pesan.setText(listPesan.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return listPesan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_tanggal, tv_pesan;
        CardView cvPesan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);
            tv_pesan = itemView.findViewById(R.id.tv_pesan);
            cvPesan = itemView.findViewById(R.id.carView_pesan);
        }
    }
}
