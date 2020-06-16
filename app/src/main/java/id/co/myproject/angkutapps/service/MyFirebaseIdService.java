package id.co.myproject.angkutapps.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.Token;

public class MyFirebaseIdService extends FirebaseInstanceIdService {
    SharedPreferences sharedPreferences;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        updateTokenToServer(refreshedToken);
    }

    private void updateTokenToServer(String refreshedToken) {
        sharedPreferences = getApplicationContext().getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        String kodeDriver = sharedPreferences.getString(Utils.KODE_DRIVER_KEY, "");
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference(Utils.token_tbl);

        Token token = new Token(refreshedToken);
        tokens.child(kodeDriver)
                .setValue(token);
    }

}
