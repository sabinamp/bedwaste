package ch.fhnw.bedwaste;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

class NetworkDetector {

    private boolean dialogShown;
    private Context context;

    private Handler networkHandler = new Handler();

    NetworkDetector(Context context) {
        this.context = context;
        this.dialogShown = false;
    }

    Runnable networkRunnable = new Runnable() {
        @Override
        public void run() {
            networkHandler.postDelayed(this, 5000);
            if (!dialogShown) {
                checkNetwork();
            }


        }
    };

    // Reacts to missing connection
    private void checkNetwork() {
        if (!isConnected()) {
            dialogShown = true;
            buildNetworkAlert().show();
        }
    }

    private boolean isConnected() {
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

    private AlertDialog.Builder buildNetworkAlert() {
        AlertDialog.Builder build = new AlertDialog.Builder(context);
        build.setTitle("Keine Internetverbindung");
        build.setMessage("Bitte überprüfen Sie Ihre Netzwerkverbindung.");
        build.setPositiveButton("Versuchen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogShown = false;
                checkNetwork();
            }
        });
        build.setCancelable(false);
        /*build.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialogShown = false;
            }
        });
        */
        return build;
    }
}