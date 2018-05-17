package com.shikanga.innovest.utils;

import okhttp3.MediaType;

public class Constants {
    public static  String CUSTOM_LOG_TAG = "CUSTOM_LOG_TAG";
//    public static String SERVER = "http://shikanga.pythonanywere.com/";
    public static String SERVER = "http://10.0.2.2:8000/";
    public static String USER_TOKEN_PATH = SERVER+"account/token/";
    public static String USER_REGISTER_PATH = SERVER+"account/user/";
    public static String AUTH_TOKEN_PREFS_KEY = "AuthToken";
    public static String AUTH_ACCOUNT_KEY = "AuthAccount";
    public static String AUTH_USER_PREFS_JSON = "AuthUser";
    public static String AUTH_PREFS_NAME = "AuthPrefs";
    public static String CATEGORY_PATH = SERVER+"category/api/";
    public static String CAMPAIGN_LIST_PATH = SERVER+"campaign/api/";
    public static String BID_LIST_PATH = SERVER+"bid/api/";
    public static String CAMPAIGN_CATEGORY_LIST_PATH = SERVER + "campaign/category/";
    public static String STATISTICS_URL = SERVER+"statistics/";

    public static String PASSWORD_RESET_PATH = SERVER + "account/password/reset/";
    public static String PASSWORD_CHANGE_PATH = SERVER + "account/password/change/";
    public static String ACCOUNT_DETAIL_PATH = SERVER+"account/api/";
    public static String ACCOUNT_DEPOSIT_PATH = SERVER+"account/deposit/request/";
    public static String ACCOUNT_WITHDRAWAL_PATH = SERVER+"account/withdraw/request/";
    public static String BID_NEW_PATH = SERVER + "bid/new/";
    public static  MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static String ACCOUNT_UPDATE_PATH = SERVER+"account/update/";
    public static String USER_DETAIL_PATH = SERVER+"account/user/";
    public static String USER_CAMPAIGNS_PATH = SERVER + "campaign/user/";

    public static String CAMPAIGN_UPDATE_PATH = SERVER+"campaign/api/";


}
