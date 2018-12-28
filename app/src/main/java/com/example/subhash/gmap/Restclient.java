package com.example.subhash.gmap;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface Restclient {
    @FormUrlEncoded
    //        I have test it with my local server it properly work
//    @POST("/gmap/send_latlng.php?")
//    Observable<Data> postData(@Field("lat") String lat ,
//                              @Field("lng") String lng);
    @POST("childcare/api/add_child.php")

    Observable<Data> postData(@Field("name") String lat ,
                              @Field("fname") String lng);




}
