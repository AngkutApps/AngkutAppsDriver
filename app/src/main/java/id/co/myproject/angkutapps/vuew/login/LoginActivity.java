package id.co.myproject.angkutapps.vuew.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.angkutapps.MainActivity;
import id.co.myproject.angkutapps.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;

import static id.co.myproject.angkutapps.helper.Utils.LOGIN_KEY;
import static id.co.myproject.angkutapps.helper.Utils.LOGIN_STATUS;

public class LoginActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        sharedPreferences = getSharedPreferences(LOGIN_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        frameLayout = findViewById(R.id.frame_login);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses...");
        cekLogin();

    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(), fragment);
        transaction.commit();
    }

    private void cekLogin(){
        progressDialog.show();
        boolean statusLogin = sharedPreferences.getBoolean(LOGIN_STATUS, false);
        if (statusLogin){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            progressDialog.dismiss();
            setFragment(new SignInFragment());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        cekLogin();
    }
}
