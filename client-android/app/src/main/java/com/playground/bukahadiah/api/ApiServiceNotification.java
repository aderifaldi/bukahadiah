package com.playground.bukahadiah.api;

import com.google.gson.JsonObject;
import com.playground.bukahadiah.model.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by aderifaldi on 08/08/2016.
 */
public interface ApiServiceNotification {

    /**
     * END POINT
     */
    String SEND = "send";

    //SEND TRANSFER EVIDENCE
    @Headers("Content-Type: application/json")
    @POST(SEND)
    Call<Notification> sendNotification(@Body JsonObject jsonPost);

}
