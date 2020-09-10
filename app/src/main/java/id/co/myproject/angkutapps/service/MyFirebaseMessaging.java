package id.co.myproject.angkutapps.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import id.co.myproject.angkutapps.helper.Utils;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessaging";
    boolean penumpangFound = false;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData() != null){
            if (!penumpangFound) {
                Map<String, String> data = remoteMessage.getData();
                String customer = data.get("customer");
                String lat = data.get("lat");
                String lng = data.get("lng");
                String idDestinasi = data.get("id_destinasi");
                String noHpUser = data.get("no_hp_user");

                Intent intent = new Intent(Utils.ORDER__RECEIVER);
                intent.putExtra("customer", customer);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("id_destinasi", idDestinasi);
                intent.putExtra("no_hp_user", noHpUser);
                LocalBroadcastManager.getInstance(MyFirebaseMessaging.this).sendBroadcast(intent);

                penumpangFound = true;
            }
        }else {
            Log.d(TAG, "onMessageReceived: YUHUUUU : Gagagl");
        }
    }
}
