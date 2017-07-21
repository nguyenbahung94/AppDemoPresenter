package com.example.nbhung.mvpmap.Service.API;


import com.example.nbhung.mvpmap.Ui.Model.Routes;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by nbhung on 6/21/2017.
 */

public interface RetrofitMap {
    @GET("maps/api/directions/json")
    Observable<Routes> getDirections(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key);

}
