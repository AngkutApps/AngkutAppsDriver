package id.co.myproject.angkutapps.view.history;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.adapter.rv_rw_perjalanan;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.data_access_object.loadView_rw_perjalanan;
import id.co.myproject.angkutapps.request.ApiRequestRiwayat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerjalananFragment extends Fragment {

    rv_rw_perjalanan perjalananAdapter;
    RecyclerView rvVoucher;

    ProgressDialog progressDialog;
    List<loadView_rw_perjalanan> loadRiwayatPerjalanan = new ArrayList<>();
    SharedPreferences sharedPreferences;

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

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Mohon Tunggu.......");
        progressDialog.show();

        sharedPreferences = getActivity().getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);

        rvVoucher = view.findViewById(R.id.rvPerjalanan);
        rvVoucher.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVoucher.setHasFixedSize(true);

        loadPerjalanan();

    }

    public void loadPerjalanan() {
        String noHpUser = sharedPreferences.getString(Utils.NOHP_DRIVER_KEY, "");

        Call<List<loadView_rw_perjalanan>> call = ApiRequestRiwayat.getInstance().getApi().getRiwayatPerjalanan(noHpUser);

        call.enqueue(new Callback<List<loadView_rw_perjalanan>>() {
            @Override
            public void onResponse(Call<List<loadView_rw_perjalanan>> call, Response<List<loadView_rw_perjalanan>> response) {
                loadRiwayatPerjalanan = response.body();
                Log.i("Hasilll", ""+response.body());

                perjalananAdapter = new rv_rw_perjalanan(getContext(), loadRiwayatPerjalanan);
                rvVoucher.setAdapter(perjalananAdapter);

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<loadView_rw_perjalanan>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}