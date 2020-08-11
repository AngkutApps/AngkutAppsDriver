package id.co.myproject.angkutapps.model.crud_table;

import android.content.Context;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

public class tb_promo {

    private Context context;

    public tb_promo(Context context) {
        this.context = context;
    }

    public void insertBeliVoucher(String kode_voucher, String no_hp){
        AndroidNetworking.post("http://angkutapps.com/angkut_api/promo/insert_promo_voucherku_driver.php")
                .addBodyParameter("kode_voucher", kode_voucher)
                .addBodyParameter("no_hp", no_hp)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Pembelian Berhasil", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Pembelian Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
