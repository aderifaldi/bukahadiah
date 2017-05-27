package com.playground.bukahadiah.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.playground.bukahadiah.model.bukalapak.BLAuthentication;

/**
 * Created by aderifaldi on 13/01/2016.
 */
public class GlobalVariable {

    public static final String SERVER_KEY = "AAAAE1Qj2Ho:APA91bFzLeD-xmlL7KZwMw01KLmBLUtEXKzBmBM6g5ZOjt-bQK3DRYdgfLQpF-CHQe__-R52eggz6UOdY64V3Gvcp32PwefZ8-0pQsw2kOaI8GkoQUX2zFnNwodfEHFS0-qNtwulCVWe";
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    public static final String PREF_NAME = "BukaHadiahPref";
    private static final String FCM_TOKEN = "fcmToken";
    private static final String NOTIFICATION_ID = "notificationId";
    private static final String TEMP_EVENT_ID = "tempEventId";
    private static final String IS_LOGIN = "igLogin";
    private static final String IS_PROFILE_UPDATED = "isProfileUpdated";
    private static final String NOTIFICATION_REQUEST_CODE = "requestCode";
    private static final String USER_ID = "userId";
    private static final String BUKALAPAK_TOKEN = "bukalapakToken";
    private static final String NAME_USER = "nameUser";
    private static final String USER_NAME = "userName";
    private static final String USER_EMAIL = "userEmail";
    private static final String USER_PROFILE_IMAGE = "userProfileImage";
    private static final String TEMP_FRIEND_FCM_TOKEN = "userProfileImage";

    public static final int REQUEST_CAMERA_FOR_TAKE_PHOTO = 4;
    public static final int REQUEST_GALERY = 5;

    public static void saveNotificationId(Context context, int data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NOTIFICATION_ID, data);
        editor.apply();
    }

    public static int getNotificationId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        int data = sharedPreferences.getInt(NOTIFICATION_ID, 0);

        return data;
    }

    public static void saveTempEventId(Context context, int data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TEMP_EVENT_ID, data);
        editor.apply();
    }

    public static int getTempEventId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        int data = sharedPreferences.getInt(TEMP_EVENT_ID, 0);

        return data;
    }

    public static void saveTempFriendFCMToken(Context context, String data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEMP_FRIEND_FCM_TOKEN, data);
        editor.apply();
    }

    public static String getTempFriendFCMToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        String data = sharedPreferences.getString(TEMP_FRIEND_FCM_TOKEN, null);

        return data;
    }

    public static void saveRequestCode(Context context, int data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NOTIFICATION_REQUEST_CODE, data);
        editor.apply();
    }

    public static int getRequestCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        int data = sharedPreferences.getInt(NOTIFICATION_REQUEST_CODE, 0);

        return data;
    }

    public static void saveFCMToken(Context context, String data){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FCM_TOKEN, data);
        editor.apply();
    }

    public static String getFCMToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        return (sharedPreferences.getString(FCM_TOKEN, null));
    }

    public static void saveUserProfileIMage(Context context, String data){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_PROFILE_IMAGE, data);
        editor.apply();
    }

    public static String getUserProfileImage(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        return (sharedPreferences.getString(USER_PROFILE_IMAGE, null));
    }

    public static void saveNameUser(Context context, String data){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME_USER, data);
        editor.apply();
    }

    public static String getNameUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        return (sharedPreferences.getString(NAME_USER, null));
    }

    public static void saveUserName(Context context, String data){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, data);
        editor.apply();
    }

    public static String getUserName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        return (sharedPreferences.getString(USER_NAME, null));
    }

    public static void saveUserEmail(Context context, String data){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_EMAIL, data);
        editor.apply();
    }

    public static String getUserEmail(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        return (sharedPreferences.getString(USER_EMAIL, null));
    }

    public static void saveUserId(Context context, int data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(USER_ID, data);
        editor.apply();
    }

    public static int getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        int data = sharedPreferences.getInt(USER_ID, 0);

        return data;
    }

    public static void saveBukalapakToken(Context context, String data){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BUKALAPAK_TOKEN, data);
        editor.apply();
    }

    public static String getBukalapakToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        return (sharedPreferences.getString(BUKALAPAK_TOKEN, null));
    }

    public static void saveIsLogin(Context context, boolean data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGIN, data);
        editor.apply();
    }

    public static boolean getIsLogin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public static void saveIsProfileUpdated(Context context, boolean data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_PROFILE_UPDATED, data);
        editor.apply();
    }

    public static boolean getIsProfileUpdated(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalVariable.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_PROFILE_UPDATED, false);
    }

    public static void saveUser(Context context, BLAuthentication userBL){
        GlobalVariable.saveUserId(context, userBL.user_id);
        GlobalVariable.saveBukalapakToken(context, userBL.token);
        GlobalVariable.saveUserEmail(context, userBL.email);
        GlobalVariable.saveNameUser(context, userBL.user_name);
    }

}
