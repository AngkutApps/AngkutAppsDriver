package id.co.myproject.angkutapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.model.data_access_object.Destinasi;

public class DestinasiAdapter extends RecyclerView.Adapter<DestinasiAdapter.ViewHolder> {
    List<Destinasi> destinasiList = new ArrayList<>();
    Context context;
    DatabaseReference db;

    public DestinasiAdapter(Context context, DatabaseReference db) {
        this.context = context;
        this.db = db;
    }

    public void setDestinasiList(List<Destinasi> destinasiList) {
        this.destinasiList.clear();
        this.destinasiList.addAll(destinasiList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DestinasiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destinasi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinasiAdapter.ViewHolder holder, int position) {
        holder.tvAlamat.setText(destinasiList.get(position).getAddress());
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.child(destinasiList.get(position).getIdDestinasi()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            removeItem(position);
                            Toast.makeText(context, "Berhasil hapus", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void removeItem(int position){
        destinasiList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return destinasiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivDelete;
        TextView tvAlamat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAlamat = itemView.findViewById(R.id.tv_alamat);
            ivDelete = itemView.findViewById(R.id.iv_delete);
        }
    }
}
