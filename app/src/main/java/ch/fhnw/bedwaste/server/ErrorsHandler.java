package ch.fhnw.bedwaste.server;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class ErrorsHandler implements FetchDataError {
    public AppCompatActivity getActivity() {
        return activity;
    }

    private final AppCompatActivity activity;

    public ErrorsHandler(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onServerError() {
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Seems our server is having a problem, try later", Toast.LENGTH_SHORT)
                        .show();
                    }
                }
        );
    }

    @Override
    public void onInternetConnectionError() {
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "There is a problem with the internet", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
        );
    }
}
