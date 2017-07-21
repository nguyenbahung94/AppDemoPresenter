package com.example.nbhung.mvpmap.Ui.Interactor;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Retrofit;

/**
 * Created by nbhung on 6/29/2017.
 */

public interface Interactor {
    void moveLocation(Location mLocation, GoogleMap mMap, Context context);

    void getDirection(Retrofit retrofit, LatLng latOrigin, LatLng latEnd, GoogleMap mMap, LoginCallback call);

    void getWeatherCurrent(Retrofit retrofit, Location myLocation, LoginCallback call);

    interface LoginCallback {
        void onfailed(String error);

        void onSuccess(String Success);
    }

}
