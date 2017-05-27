package com.playground.bukahadiah.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.playground.bukahadiah.helper.GlobalVariable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by aderifaldi on 08/08/2016.
 */
public class ServiceGeneratorNotification {

    static final String BASE_URL = "https://fcm.googleapis.com/fcm/";

    private ServiceGeneratorNotification(){}

    public static <S> S createService(Class<S> serviceClass){

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", "key=" + GlobalVariable.SERVER_KEY);
                        Request request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .build();

        Retrofit builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return builder.create(serviceClass);

    }

}