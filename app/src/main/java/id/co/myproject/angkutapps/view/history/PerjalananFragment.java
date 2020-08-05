package id.co.myproject.angkutapps.view.history;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.adapter.RiwayatAdapter;

public class PerjalananFragment extends Fragment {

    RiwayatAdapter riwayatAdapter;
    RecyclerView rvVoucher;

    public PerjalananFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perjalanan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvVoucher = view.findViewById(R.id.rvPerjalanan);
        rvVoucher.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVoucher.setHasFixedSize(true);
        riwayatAdapter = new RiwayatAdapter(getContext());
        rvVoucher.setAdapter(riwayatAdapter);
    }
}