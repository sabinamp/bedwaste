package ch.fhnw.bedwaste.server;

import android.support.v7.app.AppCompatActivity;

import ch.fhnw.bedwaste.HotelInfoActivity;

public interface FetchDataError {
    void onInternetConnectionError();
    void onServerError();
}
