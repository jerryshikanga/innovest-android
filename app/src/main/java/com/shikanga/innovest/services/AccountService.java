package com.shikanga.innovest.services;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.shikanga.innovest.models.Account;
import com.shikanga.innovest.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AccountService {

    public static Boolean requestWithdraw(int amount, int phone, Context context){
        try{
            String token = UserService.getLoggedInAuthToken(context);
            OkHttpClient client = new OkHttpClient();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount", amount);
            jsonObject.put("phone_number", phone);
            RequestBody requestBody = RequestBody.create(Constants.JSON, jsonObject.toString());
            Request request = new Request.Builder().url(Constants.ACCOUNT_WITHDRAWAL_PATH).addHeader("Authorization", "Token "+token).post(requestBody).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                return true;
            }
        }
        catch (JSONException e){

        }
        catch (IOException e){

        }
        return false;
    }

    public static Boolean requestDeposit(int amount, int phone, Context context){
        try{
            String token = UserService.getLoggedInAuthToken(context);
            OkHttpClient client = new OkHttpClient();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount", amount);
            jsonObject.put("phone_number", phone);
            RequestBody requestBody = RequestBody.create(Constants.JSON, jsonObject.toString());
            Request request = new Request.Builder().url(Constants.ACCOUNT_DEPOSIT_PATH).addHeader("Authorization", "Token "+token).post(requestBody).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                return true;
            }
        }
        catch (JSONException e){

        }
        catch (IOException e){

        }
        return false;
    }

    public static Boolean requestWithdraw(int amount,Context context){
        try{
            String token = UserService.getLoggedInAuthToken(context);
            OkHttpClient client = new OkHttpClient();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount", amount);
            RequestBody requestBody = RequestBody.create(Constants.JSON, jsonObject.toString());
            Request request = new Request.Builder().url(Constants.ACCOUNT_WITHDRAWAL_PATH).addHeader("Authorization", "Token "+token).post(requestBody).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                return true;
            }
        }
        catch (JSONException e){

        }
        catch (IOException e){

        }
        return false;
    }

    public static Boolean requestDeposit(int amount, Context context){
        try{
            String token = UserService.getLoggedInAuthToken(context);
            OkHttpClient client = new OkHttpClient();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount", amount);
            RequestBody requestBody = RequestBody.create(Constants.JSON, jsonObject.toString());
            Request request = new Request.Builder().url(Constants.ACCOUNT_DEPOSIT_PATH).addHeader("Authorization", "Token "+token).post(requestBody).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                return true;
            }
        }
        catch (JSONException e){

        }
        catch (IOException e){

        }
        return false;
    }

    public static Account getAccount(int userId, Context context){
        String token = UserService.getLoggedInAuthToken(context);
        try {
            JSONObject jsonObject = new JSONObject();
            RequestBody requestBody = RequestBody.create(Constants.JSON, jsonObject.toString());
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(Constants.ACCOUNT_DETAIL_PATH+userId+"/")
                    .addHeader("Authorization", "Token "+token)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject responseObject = new JSONObject(response.body().string());
            Gson gson = new Gson();
            Account account = gson.fromJson(responseObject.toString(), Account.class);
            return account;
        }
        catch (JSONException e){

        }
        catch (IOException e){

        }
        return null;
    }

    public static JSONObject updateAccount(String firstName, String lastName, String telephone, File profileImage, Context context){
        try {
            final MediaType MEDIA_TYPE = profileImage.getName().endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("picture", profileImage.getName() , RequestBody.create(MEDIA_TYPE, profileImage))
                    .addFormDataPart("first_name", firstName)
                    .addFormDataPart("last_name", lastName)
                    .addFormDataPart("telephone", telephone)
                    .build();

            Request request = new Request.Builder()
                    .url(Constants.ACCOUNT_UPDATE_PATH)
                    .addHeader("Authorization", "Token "+UserService.getLoggedInAuthToken(context))
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            return new JSONObject(responseBody);

        } catch (IOException e ) {
            Log.e(Constants.CUSTOM_LOG_TAG, "Error: " + e.getLocalizedMessage());
        } catch (JSONException e) {
            Log.e(Constants.CUSTOM_LOG_TAG, "Error: " + e.getLocalizedMessage());
        }
        return null;
    }
}
