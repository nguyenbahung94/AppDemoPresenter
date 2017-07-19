package com.example.nbhung.mvpmap.Ui.Interactor;

import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.example.nbhung.mvpmap.R;
import com.example.nbhung.mvpmap.Service.API.RetrofitMap;
import com.example.nbhung.mvpmap.Service.API.RetrofitWeather;
import com.example.nbhung.mvpmap.Ui.Model.InforWeather;
import com.example.nbhung.mvpmap.Ui.Model.MyRouter;
import com.example.nbhung.mvpmap.Ui.Model.Routes;
import com.example.nbhung.mvpmap.Ui.Model.leg;
import com.example.nbhung.mvpmap.Ui.Model.mainwt;
import com.example.nbhung.mvpmap.Ui.Model.overview_polylineTam;
import com.example.nbhung.mvpmap.utils.AppConstants;
import com.example.nbhung.mvpmap.utils.DecodePollyLine;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nbhung on 6/29/2017.
 */

public class InteractorImp implements Interactor {
    @Override
    public void moveLocation(Location mLocation, GoogleMap mMap) {
        Log.e("I'm here", "in the Interactor ,hello there");
        if (mLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), 15));
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.me)).position(new LatLng(mLocation.getLatitude(), mLocation.getLongitude())));
        }

    }

    @Override
    public void getDirection(Retrofit retrofit, final LatLng latOrigin, final LatLng latEnd, final GoogleMap mMap, final LoginCallback call) {
        retrofit.create(RetrofitMap.class).getDirections(latOrigin.latitude + "," + latOrigin.longitude, latEnd.latitude + "," + latEnd.longitude, AppConstants.GOOGLE_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Routes>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Routes routes) {
                        call.onSuccess("Success");
                        List<MyRouter> tam = routes.getRoutes();
                        directionOnMap(tam, mMap);
                    }
                });
    }

    @Override
    public void getWeatherCurrent(Retrofit retrofit, Location myLocation, final LoginCallback call) {
        retrofit.create(RetrofitWeather.class).getWeather(String.valueOf(myLocation.getLatitude()), String.valueOf(myLocation.getLongitude()), "metric", AppConstants.WEARTHER_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<InforWeather>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(InforWeather inforWeather) {
                        mainwt mainwt = inforWeather.getObmain();
                        Log.e("mainwt", mainwt.getTemp());
                        call.onSuccess("Nhiet do la :" + mainwt.getTemp() + "°C");

                    }
                });
    }

    public void directionOnMap(List<MyRouter> tam2, GoogleMap mMap) {
        overview_polylineTam tamss = tam2.get(0).getStrline();
        List<LatLng> latLngs = DecodePollyLine.decodePollyLine(tamss.getPoints());
        leg leg = tam2.get(0).getLegs().get(0);
        Log.e("point", tamss.getPoints());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(leg.getStartLocation().getLat(), leg.getStartLocation().getLng()), 15));
//        ((TextView) findViewById(R.id.tvDuration)).setText(leg.getDuration().getText());
//        ((TextView) findViewById(R.id.tvDistance)).setText(leg.getDistance().getText());

        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.place)).title(leg.getStartAdd()).position(new LatLng(leg.getStartLocation().getLat(), leg.getStartLocation().getLng())));
        mMap.addMarker((new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.place)).title(leg.getEndAdd()).position(new LatLng(leg.getEndLocation().getLat(), leg.getEndLocation().getLng()))));

        PolylineOptions polylineOptions = new PolylineOptions().geodesic(true).color(Color.GREEN).width(5);
        for (int i = 0; i < latLngs.size(); i++) {
            polylineOptions.add(latLngs.get(i));
        }
        mMap.addPolyline(polylineOptions);
    }

}
