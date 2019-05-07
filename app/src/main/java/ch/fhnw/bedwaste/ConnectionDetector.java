package ch.fhnw.bedwaste;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

public class ConnectionDetector {

    boolean isShown;
    Context context;

    ConnectionDetector(Context context) {
        this.context = context;
        this.isShown = false;
    }

    public void checkConnected() {
        if (!isConnected() && !isShown) {
            isShown = true;
            buildConnectionAlert().show();
        }
    }

    public boolean isConnected() {
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

    public AlertDialog.Builder buildConnectionAlert() {
        AlertDialog.Builder build = new AlertDialog.Builder(context);
        build.setTitle("No Internet Connection");
        build.setMessage("You need to have Mobile Data or wifi to use this app.");
        build.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isShown = false;
                checkConnected();
            }
        });
        build.setCancelable(false);
        /*build.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isShown = false;
            }
        });*/
        return build;
    }

    Handler connectionHandler = new Handler();

    Runnable internetRunnable = new Runnable() {
        @Override
        public void run() {
            connectionHandler.postDelayed(this, 5000);
            checkConnected();


        }
    };
}


