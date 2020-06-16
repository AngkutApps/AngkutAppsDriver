package id.co.myproject.angkutapps.helper;

import com.google.android.gms.maps.model.LatLng;

public interface OrderListener {
    void onFinishedOrder(String customer, LatLng latLngPassanger, String idDestinasiPassenger, String noHpUser);
}
