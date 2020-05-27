package id.co.myproject.angkutapps.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.model.Penumpang;

public class CostumWindowInfo implements GoogleMap.InfoWindowAdapter {

    View mView;

    public CostumWindowInfo(Context context){

        mView = LayoutInflater.from(context)
                .inflate(R.layout.custom_rider_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        ImageView ivProfil = mView.findViewById(R.id.iv_profil);
        TextView tvNama = mView.findViewById(R.id.tv_nama);
        TextView tvNoHp = mView.findViewById(R.id.tv_no_hp);

        tvNama.setText(marker.getTitle());
        tvNoHp.setText(marker.getSnippet());

        return mView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
