package id.co.myproject.angkutapps.view.history;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import id.co.myproject.angkutapps.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    TextView btnPerjalanan, btnVoucher;

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
//        getActivity().getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT );

        btnPerjalanan = view.findViewById(R.id.btnPerjalanan);
        btnVoucher = view.findViewById(R.id.btnVoucher);

        btnPerjalanan.setOnClickListener(clickListener);
        btnVoucher.setOnClickListener(clickListener);

        defaultViewButton();

    }

    public View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnPerjalanan :
                    viewButtonNormal();
                    defaultViewButton();
                    break;
                case R.id.btnVoucher :
                    viewButtonNormal();
                    btnVoucher.setBackgroundColor(Color.parseColor("#008577"));
                    btnVoucher.setTextColor(Color.parseColor("#FFFFFF"));
                    setFragment(new VoucherFragment());
                    break;
            }
        }
    };

    private void viewButtonNormal(){
        btnPerjalanan.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btnPerjalanan.setTextColor(Color.parseColor("#008577"));
        btnVoucher.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btnVoucher.setTextColor(Color.parseColor("#008577"));
    }

    private void defaultViewButton(){
        btnPerjalanan.setBackgroundColor(Color.parseColor("#008577"));
        btnPerjalanan.setTextColor(Color.parseColor("#FFFFFF"));
        setFragment(new PerjalananFragment());
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_perjalanan, fragment);
        fragmentTransaction.commit();
    }

}
