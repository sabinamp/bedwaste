package ch.fhnw.bedwaste;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    /*Context context;
    ConnectionDetector (Context context){
    this.context = context;
     */

    public void checkConnected(Context context) {
        if (!isConnected(context)) {
            buildConnectionAlert(context).show();
        }
    }

    public boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

    public AlertDialog.Builder buildConnectionAlert(final Context context) {
        AlertDialog.Builder build = new AlertDialog.Builder(context);
        build.setTitle("No Internet Connection");
        build.setMessage("You need to have Mobile Data or wifi to use this app.");
        build.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkConnected(context);
            }
        });
        return build;
    }
}


