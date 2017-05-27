package com.playground.bukahadiah.api;

import com.google.gson.JsonObject;
import com.playground.bukahadiah.model.bukahadiah.BHActivity;
import com.playground.bukahadiah.model.bukahadiah.BHEvent;
import com.playground.bukahadiah.model.bukahadiah.BHEventDetail;
import com.playground.bukahadiah.model.bukahadiah.BHGift;
import com.playground.bukahadiah.model.bukahadiah.BHGiftItem;
import com.playground.bukahadiah.model.bukahadiah.BHMemberList;
import com.playground.bukahadiah.model.bukahadiah.BHNotification;
import com.playground.bukahadiah.model.bukahadiah.BHUser;
import com.playground.bukahadiah.model.bukahadiah.BHUserDetail;
import com.playground.bukahadiah.model.bukahadiah.ModelBase;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by aderifaldi on 08/08/2016.
 */
public interface ApiServiceBH {

    /**
     * END POINT
     */

    String GIFTBOX = "GiftBoxes";
    String GIFTBOX_DETAIL = "GiftBoxes/{id}";
    String ADD_WISH = "GiftItem/{user_id}";
    String EVENT_BY_USER = "GiftBoxes";
    String MEMBER = "Members";
    String MEMBER_PROFILE = "Members/{user_blid}";
    String MEMBER_LIST = "Members/find/{user_blid}";
    String GIFT_ITEM = "GiftItem";
    String DELETE_GIFT = "GiftItem/{id}";
    String ACTIVITY = "activity/get/{user_blid}";
    String MY_ACTIVITY = "activity/getmyactivity/{user_blid}";
    String ADD_POST = "GiftPost";
    String UPDATE_PHOTO = "members/{user_id}/photo";
    String ADD_NOTIFICATION = "Notification";
    String GET_NOTIFICATION = "Notification/get/{user_id}";
    String MEMBER_FOLLOW = "Friendship";
    String GIFTBOX_BY_USER = "giftboxes/{user_id}/get";

    @Headers("Content-Type: application/json")
    @POST(GIFTBOX)
    Call<BHGift> CreateGiftBox(@Body JsonObject jsonPost);

    @Headers("Content-Type: application/json")
    @POST(MEMBER_FOLLOW)
    Call<ModelBase> FollowUser(@Body JsonObject jsonPost);

    @Headers("Content-Type: application/json")
    @POST(ADD_WISH)
    Call<ModelBase> AddWish(@Body JsonObject jsonPost, @Path("user_id") int user_id);

    @Headers("Content-Type: application/json")
    @POST(UPDATE_PHOTO)
    Call<ModelBase> UpdatePhoto(@Body JsonObject jsonPost, @Path("user_id") int user_id);

    @Headers("Content-Type: application/json")
    @POST(ADD_POST)
    Call<ModelBase> AddPost(@Body JsonObject jsonPost);

    @Headers("Content-Type: application/json")
    @POST(MEMBER)
    Call<ModelBase> AddMember(@Body JsonObject jsonPost);

    @Headers("Content-Type: application/json")
    @POST(ADD_NOTIFICATION)
    Call<ModelBase> AddNotification(@Body JsonObject jsonPost);

    @GET(MEMBER_PROFILE)
    Call<BHUser> MyProfile(@Path("user_blid") int user_blid);

    @GET(GET_NOTIFICATION)
    Call<BHNotification> GetNotification(@Path("user_id") int user_id);

    @GET(ACTIVITY)
    Call<BHActivity> GetActivity(@Path("user_blid") int user_blid);

    @GET(MY_ACTIVITY)
    Call<BHActivity> GetMyActivity(@Path("user_blid") int user_blid);

    @GET(MEMBER_PROFILE)
    Call<BHUserDetail> MemberProfile(@Path("user_blid") int user_blid,
                                     @Query("other_id") String userBlid);

    @GET(MEMBER_LIST)
    Call<BHMemberList> GetListMember(@Path("user_blid") int user_blid);

    @GET(GIFTBOX)
    Call<BHEvent> GetGiftBox(@Query("user_id") int user_id);

    @GET(GIFTBOX_DETAIL)
    Call<BHEventDetail> GetGiftBoxDetail(@Path("id") int id);

    @GET(GIFT_ITEM)
    Call<BHGiftItem> GetGiftItem(@Query("event_id") int event_id);

    @GET(GIFTBOX_BY_USER)
    Call<BHEvent> GetGiftBoxByUser(@Path("user_id") int user_id);

    @DELETE(DELETE_GIFT)
    Call<ModelBase> DeleteGift(@Path("id") int id);

}
