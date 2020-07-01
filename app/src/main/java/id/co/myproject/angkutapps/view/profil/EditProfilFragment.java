package id.co.myproject.angkutapps.view.profil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import id.co.myproject.angkutapps.BuildConfig;
import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.helper.ConvertBitmap;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.Driver;
import id.co.myproject.angkutapps.model.Value;
import id.co.myproject.angkutapps.request.ApiRequest;
import id.co.myproject.angkutapps.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfilFragment extends Fragment implements ConvertBitmap {

    public static final int REQUEST_GALERY = 96;
    public static final int REQUEST_CAMERA = 98;
    public static final int CAMERA_PERMISSION = 90;
    ImageView ivBack, ivUser, ivCamera;
    TextView tvCamera, tvGalery;
    Button btnUpdate;
    EditText etNama, etAlamat, etNoTelp, etEmail, etKtp, etMerkMobil, etPlat;
    RadioButton rbL, rbP;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    Bitmap bitmap = null;
    String photoUser = null;
    int idUser;
    ApiRequest apiRequest;

    public EditProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark) );

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);

        ivBack = view.findViewById(R.id.iv_back);
        ivUser = view.findViewById(R.id.iv_user);
        ivCamera = view.findViewById(R.id.iv_camera);
        etNama = view.findViewById(R.id.et_nama);
        etAlamat = view.findViewById(R.id.et_alamat);
        etNoTelp = view.findViewById(R.id.et_no_telp);
        etKtp = view.findViewById(R.id.et_ktp);
        etMerkMobil = view.findViewById(R.id.et_merk_mobil);
        etPlat = view.findViewById(R.id.et_plat);
        etEmail = view.findViewById(R.id.et_username);
        rbL = view.findViewById(R.id.rb_l);
        rbP = view.findViewById(R.id.rb_p);
        btnUpdate = view.findViewById(R.id.btn_update);

        radioSexGroup = view.findViewById(R.id.radioSex);

        idUser = getArguments().getInt(Utils.KODE_DRIVER_KEY);

        loadDataUser();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        });

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isConnectionInternet(getActivity())) {

                    String jk = "";
                    if (rbL.isChecked()){
                        jk = "L";
                    }else {
                        jk = "P";
                    }

                    if (photoUser != null) {
                        new LoadBitmapConverterCallback(getActivity(), EditProfilFragment.this::bitmapToString).execute();
                    }
                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Proses ...");
                    progressDialog.show();
                    Call<Value> callEditProfil = apiRequest.editProfilRequest(
                            idUser,
                            etEmail.getText().toString(),
                            etNama.getText().toString(),
                            etKtp.getText().toString(),
                            etMerkMobil.getText().toString(),
                            etPlat.getText().toString(),
                            jk,
                            photoUser,
                            etAlamat.getText().toString(),
                            etNoTelp.getText().toString()
                    );

                    callEditProfil.enqueue(new Callback<Value>() {
                        @Override
                        public void onResponse(Call<Value> call, Response<Value> response) {

                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStackImmediate();
                        }

                        @Override
                        public void onFailure(Call<Value> call, Throwable t) {
                            Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(getActivity(), "Tidak ada jaringan", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loadDataUser() {

        Call<Driver> callUser = apiRequest.userByIdRequest(idUser);
        callUser.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (response.isSuccessful()){
                    Driver user = response.body();
                    etNama.setText(user.getNama());
                    etEmail.setText(user.getEmail());
//                    etKtp.setText(user.getKtp());
                    etMerkMobil.setText(user.getMerkMobil());
                    etPlat.setText(user.getPlat());
                    etNoTelp.setText(user.getNoHp());
                    etAlamat.setText(user.getAlamat());

                    if (user.getJk().equals("L")){
                        rbL.setChecked(true);
                    }else {
                        rbP.setChecked(false);
                    }

                    Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR+"profil/"+user.getFoto()).into(ivUser);
                }
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void editProfil(String gambar){
        photoUser = gambar;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }else {
                Toast.makeText(getActivity(), "Camera permission denied", Toast.LENGTH_SHORT).show();
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
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    ivUser.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode == REQUEST_CAMERA){
            if (resultCode == RESULT_OK && data != null){
                bitmap = (Bitmap) data.getExtras().get("data");
                ivUser.setImageBitmap(bitmap);
                new LoadBitmapConverterCallback(getActivity(), EditProfilFragment.this::bitmapToString).execute();
            }
        }
    }

    @Override
    public void bitmapToString(String imgConvert) {
        editProfil(imgConvert);
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
