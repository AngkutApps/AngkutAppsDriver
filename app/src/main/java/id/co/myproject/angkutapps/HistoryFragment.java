package id.co.myproject.angkutapps;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.angkutapps.adapter.RiwayatAdapter;
import id.co.myproject.angkutapps.model.Riwayat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    Toolbar toolbar;
    RiwayatAdapter riwayatAdapter;
    RecyclerView rvHistory;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT );

        rvHistory = view.findViewById(R.id.rv_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        riwayatAdapter = new RiwayatAdapter(getActivity());
        rvHistory.setAdapter(riwayatAdapter);

        loadData();

    }

    private void loadData() {
        List<Riwayat> riwayatList = new ArrayList<>();
        riwayatList.add(new Riwayat("100K", "Makassar", "Bone", "Selasa", "26 Juni 2020"));
        riwayatList.add(new Riwayat("200K", "Pangkep", "Pinrang", "Rabu", "11 April 2020"));
        riwayatList.add(new Riwayat("150K", "Toraja", "Pangkep", "Senin", "06 Januari 2020"));
        riwayatList.add(new Riwayat("180K", "Makassar", "Palopo", "Sabtu", "09 Januari 2020"));
        riwayatList.add(new Riwayat("192K", "Gowa", "Sinjai", "Minggu", "16 Desember 2020"));
        riwayatList.add(new Riwayat("270K", "Sinjai", "Makassar", "Rabu", "20 Desember 2020"));

        riwayatAdapter.setRiwayatList(riwayatList);

    }
}
