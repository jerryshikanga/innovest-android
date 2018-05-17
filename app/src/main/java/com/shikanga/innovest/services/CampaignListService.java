package com.shikanga.innovest.services;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.shikanga.innovest.models.Campaign;
import com.shikanga.innovest.models.Category;
import com.shikanga.innovest.models.User;
import com.shikanga.innovest.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.http.Url;

public class CampaignListService {
    public static Campaign[] getListCampaign(Category category)
    {
        String url;
        if (category == null){
            url = Constants.CAMPAIGN_LIST_PATH;
        }
        else{
            url = Constants.CAMPAIGN_CATEGORY_LIST_PATH+category.getId()+"/";
        }
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
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

    public static Campaign[] getLoggedInUserCampaigns(Context context)
    {
        String url = Constants.USER_CAMPAIGNS_PATH;
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Token "+UserService.getLoggedInAuthToken(context))
                    .build();
            Response response = client.newCall(request).execute();
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


    public static JSONObject registerCampaign(Context context, String name, String summary, String description, Float amount, Date date, Category category){
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd", Locale.ENGLISH);
        try{
            JSONObject campaignJson = new JSONObject();
            campaignJson.put("name", name);
            campaignJson.put("summary", summary);
            campaignJson.put("description", description);
            campaignJson.put("amount", amount);
            campaignJson.put("end", dateFormat.format(date));
            campaignJson.put("category", category.getId());
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(JSON, campaignJson.toString());
            OkHttpClient client = new OkHttpClient();
            String credentials = "Token "+UserService.getLoggedInAuthToken(context);
            Request request = new Request.Builder().url(Constants.CAMPAIGN_LIST_PATH).post(requestBody).header("Authorization", credentials).build();
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

    public static JSONObject registerCampaign(Context context, String name, String summary, String description, Float amount, Date date, Category category, File imageFile){
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd", Locale.ENGLISH);
        String url = Constants.CAMPAIGN_LIST_PATH;
        try{
            final MediaType MEDIA_TYPE = imageFile.getName().endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("picture", imageFile.getName() , RequestBody.create(MEDIA_TYPE, imageFile))
                    .addFormDataPart("name", name)
                    .addFormDataPart("summary", summary)
                    .addFormDataPart("description", description)
                    .addFormDataPart("amount", String.valueOf(amount))
                    .addFormDataPart("category", String.valueOf(category.getId()))
                    .addFormDataPart("end", dateFormat.format(date))
                    .build();
            OkHttpClient client = new OkHttpClient();
            String credentials = "Token "+UserService.getLoggedInAuthToken(context);
            Request request = new Request.Builder().url(url).post(requestBody).header("Authorization", credentials).build();
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

    public static JSONObject updateCampaign(Context context, Campaign campaign, String name, String summary, String description, Float amount, Date date, Category category, File imageFile){
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd", Locale.ENGLISH);
        try{
            URL url = new URL(Constants.CAMPAIGN_UPDATE_PATH + campaign.getId() +"/");
            final MediaType MEDIA_TYPE = imageFile.getName().endsWith("png")
                    ? MediaType.parse("image/png")
                    : MediaType.parse("image/jpeg");

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("picture", imageFile.getName() , RequestBody.create(MEDIA_TYPE, imageFile))
                    .addFormDataPart("name", name)
                    .addFormDataPart("summary", summary)
                    .addFormDataPart("description", description)
                    .addFormDataPart("amount", String.valueOf(amount))
                    .addFormDataPart("category", String.valueOf(category.getId()))
                    .addFormDataPart("end", dateFormat.format(date))
                    .build();
            OkHttpClient client = new OkHttpClient();
            String credentials = "Token "+UserService.getLoggedInAuthToken(context);
            Request request = new Request.Builder()
                    .url(url)
                    .put(requestBody)
                    .header("Authorization", credentials)
                    .build();
            Response response = client.newCall(request).execute();
            String responseStr = response.body().string();
            Log.i(Constants.CUSTOM_LOG_TAG, responseStr);
            JSONObject serverJson = new JSONObject(responseStr);
            return serverJson;
        }
        catch (JSONException e){

        }
        catch (IOException e){

        }
        return  null;
    }
}
