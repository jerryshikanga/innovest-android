package com.shikanga.innovest.services;

import android.util.Log;

import com.google.gson.Gson;
import com.shikanga.innovest.models.Campaign;
import com.shikanga.innovest.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CampaignListService {
    public static Campaign[] getListCampaign()
    {
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(Constants.CAMPAIGN_LIST_PATH).build();
            Response response = client.newCall(request).execute();
//            Log.i(Constants.CUSTOM_LOG_TAG, "Gotten response : "+response.body().string());
            JSONArray jsonArray = new JSONArray(response.body().string());
            Gson gson = new Gson();
            Campaign[] campaignList = gson.fromJson(jsonArray.toString(), Campaign[].class);
            return campaignList;
        }
        catch (IOException e){

        }
        catch (JSONException e){

        }
        return null;
    }
}
