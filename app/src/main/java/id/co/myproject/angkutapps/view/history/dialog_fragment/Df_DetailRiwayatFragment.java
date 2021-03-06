package id.co.myproject.angkutapps.view.history.dialog_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.model.data_access_object.loadView_rw_perjalanan;
import id.co.myproject.angkutapps.view.profil.dialog_fragment.Df_chat;

public class Df_DetailRiwayatFragment extends DialogFragment {

    LinearLayout btn_chat;
    ImageView imgClose;
    View imgProfil;

    TextView tvDari, tvTujuan, tvHari, tvHargaPerjalanan, tvTglBerangkat, tvTglSampai, tvNamaPenumpang,
            tvJenisKelamin, tvPenumpangDewasa, tvPenumpangAnak;

    loadView_rw_perjalanan loadData;

    public Df_DetailRiwayatFragment(loadView_rw_perjalanan loadView_rw_perjalanan) {
        this.loadData = loadView_rw_perjalanan;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frame_detail_riwayat, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDari = view.findViewById(R.id.tvDari);
        tvTujuan = view.findViewById(R.id.tvTujuan);
        tvHari = view.findViewById(R.id.tvHariKeberangkatan);
        tvHargaPerjalanan = view.findViewById(R.id.tvHargaPerjalanan);
        tvTglBerangkat = view.findViewById(R.id.tvTglBerangkat);
        tvTglSampai = view.findViewById(R.id.tvTglSampai);
        tvNamaPenumpang = view.findViewById(R.id.tvNamaPenumpang);
        tvJenisKelamin = view.findViewById(R.id.tvJenisKelamin);
        tvPenumpangDewasa = view.findViewById(R.id.tvPenumpangDewasa);
        tvPenumpangAnak = view.findViewById(R.id.tvPenumpangAnak);
        btn_chat = view.findViewById(R.id.btn_chat);

        imgProfil = view.findViewById(R.id.oval);
        imgClose = view.findViewById(R.id.imgClose);

        tvDari.setText(loadData.getDari());
        tvTujuan.setText(loadData.getTujuan());
        tvHari.setText(loadData.getHari_keberangkatan());
//        tvHargaPerjalanan.setText(""+loadData.getBiaya());
        tvTglBerangkat.setText(loadData.getTgl_berangkat());
        tvTglSampai.setText(loadData.getTgl_sampai());
        tvNamaPenumpang.setText(loadData.getNama_user());
        tvJenisKelamin.setText(""+loadData.getJenis_kelamin());
        tvPenumpangDewasa.setText(""+loadData.getPenumpang_dewasa());
        tvPenumpangAnak.setText(""+loadData.getPenumpang_anak());

        int panjang = loadData.getBiaya();
        if (panjang>9999 && panjang<99999){
            tvHargaPerjalanan.setText(String.valueOf(loadData.getBiaya()).substring(0, 2)+"k");
        }else if (panjang>99999 && panjang<999999){
            tvHargaPerjalanan.setText(String.valueOf(loadData.getBiaya()).substring(0, 3)+"k");
        }else if (panjang>999999){
            tvHargaPerjalanan.setText(String.valueOf(loadData.getBiaya()).substring(0, 4)+"k");
        }

//        if (loadView.getTransportasi().equals("bus"))
//            imgProfil.setBackgroundResource(R.drawable.shape_oval_bus);
//        else if (loadView.getTransportasi().equals("pete"))
//            imgProfil.setBackgroundResource(R.drawable.shape_oval_pete);
//        else if (loadView.getTransportasi().equals("travel"))
//            imgProfil.setBackgroundResource(R.drawable.shape_oval_travel);
        imgClose.setOnClickListener(clickListener);
        btn_chat.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imgClose :
                    Df_DetailRiwayatFragment.super.onStop();
                    Df_DetailRiwayatFragment.super.onDestroyView();
                    break;
                case R.id.btn_chat :
                    setFragment(new Df_chat(loadData.getNo_hp(), loadData.getNama_user(), loadData.getTujuan()));
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void setFragment(DialogFragment fragment){
        FragmentManager fragmentManager = ((FragmentActivity)getContext()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("dialog");
        if (prev !=null){
            fragmentTransaction.remove(prev);
        }
        fragment.show(fragmentTransaction, "dialog");
    }
}
