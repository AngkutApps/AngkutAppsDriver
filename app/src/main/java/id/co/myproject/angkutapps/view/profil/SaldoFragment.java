package id.co.myproject.angkutapps.view.profil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.myproject.angkutapps.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaldoFragment extends Fragment {

    public SaldoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saldo, container, false);
    }
}
