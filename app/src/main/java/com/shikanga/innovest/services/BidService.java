package com.shikanga.innovest.services;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shikanga.innovest.models.Bid;
import com.shikanga.innovest.models.Campaign;
import com.shikanga.innovest.models.Category;
import com.shikanga.innovest.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BidService {
    public static Bid[] getListBid(Context context)
    {
        Gson gson = new Gson();
        String token = UserService.getLoggedInAuthToken(context);
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(Constants.BID_LIST_PATH).header("Authorization", "Token "+token).build();
            Response response = client.newCall(request).execute();
            JSONArray jsonArray = new JSONArray(response.body().string());
            Bid[] bids = gson.fromJson(jsonArray.toString(), Bid[].class);
            return bids;
        }
        catch (IOException e){

        }
        catch (JSONException e){

        }
        return  null;
    }

    public static JSONObject placeBid(float amount, Campaign campaign, Context context)
    {
        try{
            JSONObject campaignJson = new JSONObject();
            campaignJson.put("amount", amount);
            campaignJson.put("campaign", campaign.getId());

            RequestBody requestBody = RequestBody.create(Constants.JSON, campaignJson.toString());
            OkHttpClient client = new OkHttpClient();
            String credentials = "Token "+UserService.getLoggedInAuthToken(context);
            Request request = new Request.Builder().url(Constants.BID_NEW_PATH).post(requestBody).header("Authorization", credentials).build();
            Response response = client.newCall(request).execute();
            JSONObject serverJson = new JSONObject(response.body().string());
            return serverJson;
        }
        catch (JSONException e){

        }
        catch (IOException e){

        }
        return  null;
    }
}
