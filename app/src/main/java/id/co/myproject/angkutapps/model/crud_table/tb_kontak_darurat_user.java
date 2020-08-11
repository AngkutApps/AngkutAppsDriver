package id.co.myproject.angkutapps.model.crud_table;

import android.content.Context;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

public class tb_kontak_darurat_user {

    private Context context;
    private String no_hp;

    public tb_kontak_darurat_user(Context context, String no_hp) {
        this.context = context;
        this.no_hp = no_hp;
    }

    public void tambahKontakDarurat(String nama, String hubungan, String nomor){
        AndroidNetworking.post("http://angkutapps.com/angkut_api/insert_kontak_darurat_user.php")
                .addBodyParameter("no_hp", no_hp)
                .addBodyParameter("nama_kontak", nama)
                .addBodyParameter("hubungan_kontak", hubungan)
                .addBodyParameter("nomor_kontak_darurat", nomor)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Kontak Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Kontak Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
                    }
                });
    }
//
//    public void deleteKontakDarurat(String nomor){
//        AndroidNetworking.post("http://angkutapps.com/angkut_api/delete_kontak_darurat_user.php")
//                .addBodyParameter("nomor_kontak_darurat", nomor)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Toast.makeText(context, "Kontak Berhasil Terhapus", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        Toast.makeText(context, "Kontak Gagal Terhapus", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    public void updateKontakDarurat(String nama, String hubungan, String nomor){
//        AndroidNetworking.post("http://angkutapps.com/angkut_api/update_kontak_darurat_user.php")
//                .addBodyParameter("no_hp", no_hp)
//                .addBodyParameter("nama_kontak", nama)
//                .addBodyParameter("hubungan_kontak", hubungan)
//                .addBodyParameter("nomor_kontak_darurat", nomor)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Toast.makeText(context, "Kontak Berhasil Diubah", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        Toast.makeText(context, "Kontak Gagal Diubah", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

//    ArrayList<LoadKontakDarurat> loadKontakDarurats = new ArrayList<>();
//
//    public ArrayList<LoadKontakDarurat> readKontakDarurat() {
//        AndroidNetworking.get("http://angkutapps.com/angkut_api/read_kontak_darurat_user.php")
//                .addQueryParameter("no_hp", "82397147928")
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONArray(new JSONArrayRequestListener() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject data = response.getJSONObject(i);
////                                    //adding the product to product list
//                                loadKontakDarurats.add(new LoadKontakDarurat(
//                                        data.getString("nama_kontak"),
//                                        data.getString("hubungan_kontak"),
//                                        data.getString("nomor_kontak_darurat")
//                                ));
//                            }
//                            Log.i("KontakDarurat",""+response);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        error.printStackTrace();
//                    }
//                });
//        return loadKontakDarurats;
//
//    }

}
