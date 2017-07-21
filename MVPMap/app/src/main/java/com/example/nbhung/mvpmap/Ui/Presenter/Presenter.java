package com.example.nbhung.mvpmap.Ui.Presenter;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Retrofit;

/**
 * Created by nbhung on 6/29/2017.
 */

public interface Presenter {
    void getLocation(Location myLocation, GoogleMap mMap, Context context);

    void direction(Retrofit retrofit, LatLng latOrigin, LatLng latEnd, GoogleMap mMap);

    void getWeather(Retrofit retrofit, Location myLocation);

    void showWeather(String temp);

}
