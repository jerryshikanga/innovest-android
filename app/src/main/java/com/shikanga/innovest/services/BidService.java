package com.shikanga.innovest.services;

import android.content.Context;

import com.google.gson.Gson;
import com.shikanga.innovest.models.Bid;
import com.shikanga.innovest.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BidService {
    public static Bid[] getListBid(Context context)
    {
        Gson gson = new Gson();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(Constants.BID_LIST_PATH).addHeader("Authorisation", "Token "+UserService.getLoggedInAuthToken(context)).build();
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
}
