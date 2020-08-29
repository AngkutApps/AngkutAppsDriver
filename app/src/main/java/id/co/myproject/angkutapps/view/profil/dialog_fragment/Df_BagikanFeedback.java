package id.co.myproject.angkutapps.view.profil.dialog_fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import id.co.myproject.angkutapps.R;

public class Df_BagikanFeedback extends BottomSheetDialogFragment {

    RelativeLayout btnFeedbackYes, btnFeedbackNo;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheets_bagikan_feedback, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnFeedbackYes = view.findViewById(R.id.btnFeedbackYes);
        btnFeedbackNo = view.findViewById(R.id.btnFeedbackNo);

        btnFeedbackYes.setOnClickListener(clickListener);
        btnFeedbackNo.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnFeedbackYes :
                    Toast.makeText(getActivity(), "Test Yes", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnFeedbackNo :
                    Toast.makeText(getActivity(), "Test No", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
