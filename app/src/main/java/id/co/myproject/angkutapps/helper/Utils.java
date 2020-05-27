package id.co.myproject.angkutapps.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
    public static final String LOGIN_KEY = "login";
    public static final String ID_USER_KEY = "id_user";
    public static final String LOGIN_STATUS = "status_login";

    public static String getDayName(int day){
        switch(day){
            case 1:
                return "Minggu";
            case 2:
                return "Senin";
            case 3:
                return "Selasa";
            case 4:
                return "Rabu";
            case 5:
                return "Kamis";
            case 6:
                return  "Jumat";
            case 7:
                return "Sabtu";
        }

        return "Salah hari";
    }

    public static int getDayNumber(String day){
        switch(day){
            case "Minggu":
                return 1;
            case "Senin":
                return 2;
            case "Selasa":
                return 3;
            case "Rabu":
                return 4;
            case "Kamis":
                return 5;
            case "Jumat":
                return 6;
            case "Sabtu":
                return 7;
        }

        return 0;
    }

    public static boolean isConnectionInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null){
                for (int i=0; i<info.length; i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

//    private void statusBarTransparent(Context context){
//        getActivity().getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT );
//
//    }

}
