package id.co.myproject.angkutapps.view.profil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.co.myproject.angkutapps.BuildConfig;
import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.adapter.rw_voucher_penggunaan;
import id.co.myproject.angkutapps.helper.ConvertBitmap;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.data_access_object.DataDriver;
import id.co.myproject.angkutapps.model.data_access_object.Driver;
import id.co.myproject.angkutapps.model.data_access_object.Value;
import id.co.myproject.angkutapps.model.data_access_object.loadView_rw_voucher_penggunaan;
import id.co.myproject.angkutapps.request.ApiRequestDataDriver;
import id.co.myproject.angkutapps.request.ApiRequestRiwayat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilUser extends AppCompatActivity implements ConvertBitmap {

    private static final String TAG = "ProfilUser";

    public static final int REQUEST_GALERY = 96;
    public static final int REQUEST_CAMERA = 98;
    public static final int CAMERA_PERMISSION = 90;

    ImageButton btnBack;
    ImageView ivCamera;
    CircleImageView ivUser;
    TextView tv_nama_driver, tv_jk_driver, tv_merk_mobil, tv_jenis_kendaraan, tv_plat_mobil, tv_warna_kendaraan;
    EditText etAlamat, etNomorHp;
    Button btnSave;
    SharedPreferences sharedPreferences;
    String noHpUser;
//    ApiRequest apiRequest;
    Bitmap bitmap = null;
    String photoUser = null;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_user);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Tunggu.....");

//        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        noHpUser = sharedPreferences.getString(Utils.NOHP_DRIVER_KEY, "");

        btnBack = findViewById(R.id.button_back);
        ivUser = findViewById(R.id.iv_user);
        ivCamera = findViewById(R.id.iv_camera);

        tv_nama_driver = findViewById(R.id.tv_nama_driver);
        tv_jk_driver = findViewById(R.id.tv_jk_driver);
        tv_merk_mobil = findViewById(R.id.tv_merk_mobil);
        tv_jenis_kendaraan = findViewById(R.id.tv_jenis_kendaraan);
        tv_plat_mobil = findViewById(R.id.tv_plat_mobil);
        tv_warna_kendaraan = findViewById(R.id.tv_warna_kendaraan);

        etAlamat = findViewById(R.id.et_alamat);
        etNomorHp = findViewById(R.id.et_nomor_hp);
        btnSave = findViewById(R.id.btn_save);
        btnBack.setOnClickListener(clickListener);
        ivCamera.setOnClickListener(clickListener);
        ivUser.setOnClickListener(clickListener);
        btnSave.setOnClickListener(clickListener);

        etNomorHp.setText(noHpUser);

        progressDialog.show();
        loadDataDriver();

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_back :
                    finish();
                    break;
                case R.id.iv_user :
                    showCameraGallery();
                    break;
                case R.id.iv_camera :
                    showCameraGallery();
                    break;
                case R.id.btn_save :
                    updateProfil();
            }
        }
    };

    private void updateProfil() {
        if (photoUser == null) {
            new LoadBitmapConverterCallback(this, ProfilUser.this::bitmapToString).execute();
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ...");
        progressDialog.show();

        Driver driver = new Driver();
        driver.setNoHp(noHpUser);
        driver.setEmail(etAlamat.getText().toString());
        driver.setFoto(photoUser);

        Call<Value> editProfilProses = ApiRequestDataDriver.getInstance().getApi().editProfilDriver(driver);
        editProfilProses.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                progressDialog.dismiss();
                Toast.makeText(ProfilUser.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                if (response.body().getValue() == 1){
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(ProfilUser.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCameraGallery() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih");
        String[] pilihJenis = {"Camera", "Galery"};
        builder.setItems(pilihJenis, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                }else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, REQUEST_GALERY);
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void loadDataDriver() {
        Call<List<DataDriver>> call = ApiRequestDataDriver.getInstance().getApi().getDataDriver(noHpUser);

        call.enqueue(new Callback<List<DataDriver>>() {
            @Override
            public void onResponse(Call<List<DataDriver>> call, Response<List<DataDriver>> response) {

                Log.d(TAG, "onResponse: Mantap Djiwa : "+response.body().size());

                DataDriver data = response.body().get(0);

                tv_nama_driver.setText(data.getNama_driver());
                if (data.getJenis_kelamin()=='L'){
                    tv_jk_driver.setText("Laki-laki");
                }else{
                    tv_jk_driver.setText("Perempuan");
                }
                tv_merk_mobil.setText(data.getMerk_mobil());
                tv_jenis_kendaraan.setText(data.getNama_jenis_kendaraan());
                tv_plat_mobil.setText(data.getPlat_mobil());
                tv_warna_kendaraan.setText(data.getWarna_kendaraan());
                etAlamat.setText(data.getEmail());

                Glide.with(ProfilUser.this).load(BuildConfig.BASE_URL_GAMBAR+"profil/"+data.getFoto()).into(ivUser);

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<DataDriver>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

//    private void loadData(){
//        Call<User> userCall = apiRequest.penumpangByIdRequest(noHpUser);
//        userCall.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful()){
//                    User user = response.body();
//                    int fotoUser;
//                    if(user.getFoto().equals("")){
//                        if (user.getJk().equals("L")){
//                            fotoUser = R.drawable.person_male;
//                        }else {
//                            fotoUser = R.drawable.person_female;
//                        }
//                        Glide.with(ProfilUser.this).load(fotoUser)
//                                .into(ivUser);
//                    }else {
//                        Glide.with(ProfilUser.this).load(BuildConfig.BASE_URL_GAMBAR+"profil/"+user.getFoto())
//                                .into(ivUser);
//                    }
//                    etAlamat.setText(user.getAlamat());
//                    etNomorHp.setText(user.getNoHp());
//                    tvUser.setText(user.getNama());
//                    if (user.getJk().equals("L")){
//                        tvJk.setText("Laki-Laki");
//                    }else {
//                        tvJk.setText("Perempuan");
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(ProfilUser.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GALERY){
            if(resultCode == RESULT_OK && data != null){
                Uri imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    ivUser.setImageBitmap(bitmap);
                    new LoadBitmapConverterCallback(this, ProfilUser.this::bitmapToString).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode == REQUEST_CAMERA){
            if (resultCode == RESULT_OK && data != null){
                bitmap = (Bitmap) data.getExtras().get("data");
                ivUser.setImageBitmap(bitmap);
                new LoadBitmapConverterCallback(this, ProfilUser.this::bitmapToString).execute();
            }
        }
    }

    @Override
    public void bitmapToString(String imgConvert) {
        photoUser = imgConvert;
    }

    private class LoadBitmapConverterCallback extends AsyncTask<Void, Void, String> {

        private WeakReference<Context> weakContext;
        private ConvertBitmap convertBitmap;

        public LoadBitmapConverterCallback(Context context, ConvertBitmap convertBitmap){
            this.weakContext = new WeakReference<>(context);
            this.convertBitmap = convertBitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakContext.get();
        }

        @Override
        protected String doInBackground(Void... voids) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] imgByte = byteArrayOutputStream.toByteArray();
            String imgBitmap = Base64.encodeToString(imgByte, Base64.DEFAULT);
            return imgBitmap;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            convertBitmap.bitmapToString(s);
        }
    }

}
