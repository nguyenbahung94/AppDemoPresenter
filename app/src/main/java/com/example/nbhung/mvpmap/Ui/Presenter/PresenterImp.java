package com.example.nbhung.mvpmap.Ui.Presenter;

import android.location.Location;

import com.example.nbhung.mvpmap.Ui.Interactor.Interactor;
import com.example.nbhung.mvpmap.Ui.Interactor.InteractorImp;
import com.example.nbhung.mvpmap.Ui.View.MainView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by nbhung on 6/29/2017.
 */

public class PresenterImp implements Presenter, Interactor.LoginCallback {
    private InteractorImp interactorImp;
    private MainView view;

    @Inject
    public PresenterImp(MainView view, InteractorImp interactorImp) {
        this.view = view;
        this.interactorImp = interactorImp;
    }

    @Override
    public void getLocation(Location myLocation, GoogleMap mMap) {
        interactorImp.moveLocation(myLocation, mMap);
    }

    @Override
    public void direction(Retrofit retrofit, LatLng latOrigin, LatLng latEnd, GoogleMap mMap) {
        interactorImp.getDirection(retrofit, latOrigin, latEnd, mMap, this);
    }

    @Override
    public void getWeather(Retrofit retrofit, Location myLocation) {
        interactorImp.getWeatherCurrent(retrofit, myLocation, this);
    }

    @Override
    public void showWeather(String temp) {
        view.showWeather(temp);
    }


    @Override
    public void onfailed(String error) {

    }

    @Override
    public void onSuccess(String Success) {
        view.showWeather(Success);
    }
}
