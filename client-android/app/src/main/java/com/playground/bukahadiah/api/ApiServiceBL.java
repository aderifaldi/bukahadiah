package com.playground.bukahadiah.api;

import com.google.gson.JsonObject;
import com.playground.bukahadiah.model.Notification;
import com.playground.bukahadiah.model.bukalapak.BLAuthentication;
import com.playground.bukahadiah.model.bukalapak.BLProduct;
import com.playground.bukahadiah.model.bukalapak.BLProductCategory;
import com.playground.bukahadiah.model.bukalapak.BLProductDetail;
import com.playground.bukahadiah.model.bukalapak.BLProductReview;
import com.playground.bukahadiah.model.bukalapak.BLProvince;
import com.playground.bukahadiah.model.bukalapak.BLRegister;
import com.playground.bukahadiah.model.bukalapak.BLUser;
import com.playground.bukahadiah.model.bukalapak.BLUserProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by aderifaldi on 08/08/2016.
 */
public interface ApiServiceBL {

    /**
     * END POINT
     */
    String CITY_BY_PROVINCE = "send";

    String LOGIN = "authenticate.json";
    String USER_INFO = "users/info.json";
    String PRODUCT_CATEGORY = "categories.json";
    String PRODUCT_LIST = "products.json";
    String POPULAR_PRODUCT = "products/populars.json";
    String DETAIL_PRODUCT = "products/{id}.json";
    String RELATED_PRODUCT = "products/{id}/related.json";
    String PRODUCT_REVIEWS = "products/{id}/reviews.json";
    String USER_REGISTER = "users.json";
    String GET_TRANSACTION = "transactions/{id}.json";
    String GET_PROVINCE = "address/provinces.json";

    //SEND TRANSFER EVIDENCE
    @Headers("Content-Type: application/json")
    @POST(CITY_BY_PROVINCE)
    Call<Notification> sendNotification(@Body JsonObject jsonPost);

    @POST(LOGIN)
    Call<BLAuthentication> Login(@Header("Authorization") String auth);

    @GET(USER_INFO)
    Call<BLUserProfile> GetUserInfo(@Header("Authorization") String auth);

    @GET(PRODUCT_CATEGORY)
    Call<BLProductCategory> GetProductCategory();

    @GET(PRODUCT_LIST)
    Call<BLProduct> SearchProduct(@Query("keywords") String keywords,
                                  @Query("page") int page,
                                  @Query("per_page") int per_page);

    @GET(PRODUCT_LIST)
    Call<BLProduct> ProductByCategory(@Query("keywords") String keywords,
                                      @Query("category_id") int category_id,
                                      @Query("page") int page,
                                      @Query("per_page") int per_page);

    @GET(DETAIL_PRODUCT)
    Call<BLProductDetail> GetDetailProduct(@Path("id") String id);

    @GET(RELATED_PRODUCT)
    Call<BLProduct> GetRelatedProduct(@Path("id") String id);

    @GET(PRODUCT_REVIEWS)
    Call<BLProductReview> GetProductReviews(@Path("id") String id);

    @POST(USER_REGISTER)
    Call<BLRegister> RegisterUser(@Body JsonObject jsonPost);

    @GET(GET_PROVINCE)
    Call<BLProvince> GetProvince();

}
