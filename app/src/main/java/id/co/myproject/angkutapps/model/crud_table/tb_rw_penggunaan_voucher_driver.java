package id.co.myproject.angkutapps.model.crud_table;

import android.content.Context;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

public class tb_rw_penggunaan_voucher_driver {

    Context context;

    public tb_rw_penggunaan_voucher_driver(Context context) {
        this.context = context;
    }

    public void deleteRiwayatPenggunaanVoucher(String id){
        AndroidNetworking.post("http://angkutapps.com/angkut_api/riwayat/delete_rw_voucher_penggunaan_driver.php")
                .addBodyParameter("id_penggunaan_voucher", id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Riwayat Berhasil Terhapus", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Riwayat Gagal Terhapus", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
