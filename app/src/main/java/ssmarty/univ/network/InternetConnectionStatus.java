package ssmarty.univ.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnectionStatus {
    public boolean checkConnection (Activity activity){


        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
//        ConnectivityManager cm = (ConnectivityManager)activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo nInfo = cm.getActiveNetworkInfo();
//        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
 //       return connected;

    }
}
