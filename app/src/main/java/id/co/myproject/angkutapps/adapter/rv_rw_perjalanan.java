package id.co.myproject.angkutapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.model.data_access_object.loadView_rw_perjalanan;

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

    }

    @Override
    public int getItemCount() {
        return listPerjalanan.size();
    }

    public class ViewHolder  extends  RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
