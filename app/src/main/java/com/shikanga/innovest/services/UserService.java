package com.shikanga.innovest.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shikanga.innovest.models.Account;
import com.shikanga.innovest.models.User;
import com.shikanga.innovest.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserService {
    public static JSONObject getAuthToken(String username, String password){
        try{
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject credential = new JSONObject().put("username",username).put("password", password);
            RequestBody body = RequestBody.create(JSON, credential.toString());
            Request request = new Request.Builder().url(Constants.USER_TOKEN_PATH).post(body).build();
            Response response = client.newCall(request).execute();

            JSONObject responseObject = new JSONObject(response.body().string());

            return responseObject;
        }
        catch (JSONException e){

        }
        catch (IOException e){

        }
        return  null;
    }

    public static Boolean setAuthTokenPref(Context context, String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.AUTH_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.AUTH_TOKEN_PREFS_KEY, token);
        editor.apply();
        return  true;
    }

    public static Boolean setAccountPref(Context context, Account account){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.AUTH_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(Constants.AUTH_ACCOUNT_KEY, gson.toJson(account));
        editor.apply();
        return true;
    }

    public static Boolean setUserPref(Context context,User user)
    {
        Gson gson = new Gson();
        SharedPreferences preferences = context.getSharedPreferences(Constants.AUTH_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = preferences.edit();
        editor.putString(Constants.AUTH_USER_PREFS_JSON, gson.toJson(user));
        editor.apply();
        return true;
    }

    public static boolean logoutUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.AUTH_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        return  true;
    }

    public static String getLoggedInAuthToken(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.AUTH_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.AUTH_TOKEN_PREFS_KEY, null);
    }

    public static Account getLoggedInAccount(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.AUTH_PREFS_NAME, Context.MODE_PRIVATE);
        String accountStr = sharedPreferences.getString(Constants.AUTH_ACCOUNT_KEY, null);
        Gson gson = new Gson();
        Account account = gson.fromJson(accountStr, Account.class);
        return account;
    }

    public static User getLoggedInUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.AUTH_PREFS_NAME, Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(Constants.AUTH_USER_PREFS_JSON, null);
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        return user;
    }

    public static JSONObject registerUser(String username, String email, String password1, String password2, String first_name, String last_name){
        try{
            OkHttpClient client = new OkHttpClient();
            JSONObject data = new JSONObject();
            data.put("username", username)
                    .put("email", email)
                    .put("password1", password1)
                    .put("password2", password2)
                    .put("last_name", last_name)
                    .put("first_name", first_name);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data.toString());
            Request request = new Request.Builder().url(Constants.USER_REGISTER_PATH).post(body).build();
            Response response = client.newCall(request).execute();
            if (response.body() != null)
            {
                JSONObject serverJSON = new JSONObject(response.body().string());
                return serverJSON;
            }
        }
        catch (JSONException e){

        }
        catch (IOException e){

        }
        return null;
    }

    public static Boolean resetPassword(String email)
    {
        try{
            OkHttpClient client = new OkHttpClient();
            JSONObject data = new JSONObject();
            data.put("email", email);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data.toString());
            Request request = new Request.Builder().url(Constants.PASSWORD_RESET_PATH).post(requestBody).build();
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

    public static JSONObject changePassword(String old_password, String new_password, Context context){
        try{
            OkHttpClient client = new OkHttpClient();
            JSONObject data = new JSONObject();
            data.put("old_password", old_password);
            data.put("new_password", new_password);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data.toString());
            Request request = new Request.Builder().url(Constants.PASSWORD_CHANGE_PATH).addHeader("Authorization", "Token "+UserService.getLoggedInAuthToken(context)).post(requestBody).build();
            Response response = client.newCall(request).execute();

            JSONObject jsonObject = new JSONObject(response.body().string());
            return jsonObject;
        }
        catch (JSONException e){

        }
        catch (IOException e){

        }
        return null;
    }

    public static User getUserById(int userId){
        try {
            String url = Constants.USER_DETAIL_PATH + userId +"/";
            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = httpClient.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            Gson gson =new Gson();
            User user = gson.fromJson(jsonObject.toString(), User.class);
            return user;
        }
        catch (IOException e){

        }
        catch (JSONException e){

        }
        return null;
    }
}
