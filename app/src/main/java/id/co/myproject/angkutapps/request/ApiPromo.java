package id.co.myproject.angkutapps.request;

import java.util.List;

import id.co.myproject.angkutapps.model.data_access_object.LoadSKVoucher;
import id.co.myproject.angkutapps.model.data_access_object.LoadVoucher;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiPromo {

    @GET("read_promo_beli_voucher_driver.php")
    Call<List<LoadVoucher>> getPromoBeliVoucher(
//            @Query("no_hp") String no_hp
    );
//
    @GET("read_sk_beli_voucher.php")
    Call<List<LoadSKVoucher>> getSKBeliVoucher(
            @Query("kode_voucher") String kode_voucher
    );
//
    @GET("read_promo_voucherku_driver.php")
    Call<List<LoadVoucher>> getPromoVoucherku(
            @Query("no_hp") String no_hp
    );

}
