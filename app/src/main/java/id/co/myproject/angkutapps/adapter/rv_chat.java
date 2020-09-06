package id.co.myproject.angkutapps.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
            holder.cvPesan.setBackgroundResource(R.color.colorChatPrimary);
            holder.lay_chat.setPadding(90,9,10,9);
            holder.lay_card.setGravity(Gravity.END);
            holder.tv_pesan.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        } else {
            holder.cvPesan.setBackgroundResource(R.color.colorChatAbuAbu);
            holder.lay_chat.setPadding(10,9,90,9);
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
        LinearLayout lay_chat, lay_card;
        LinearLayout.LayoutParams params;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);
            tv_pesan = itemView.findViewById(R.id.tv_pesan);
            cvPesan = itemView.findViewById(R.id.carView_pesan);
            lay_card = itemView.findViewById(R.id.lay_card);
            lay_chat = itemView.findViewById(R.id.lay_chat);

            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lay_chat.setLayoutParams(params);
        }
    }
}
