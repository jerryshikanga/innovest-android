package com.shikanga.innovest.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shikanga.innovest.models.User;
import com.shikanga.innovest.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

    public static Boolean loginUser(Context context, String token, User user, String picture)
    {
        Gson gson = new Gson();
        SharedPreferences preferences = context.getSharedPreferences(Constants.AUTH_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = preferences.edit();
        editor.putString(Constants.AUTH_PICTURE_KEY, picture);
        editor.putString(Constants.AUTH_TOKEN_PREFS_KEY, token);
        editor.putString(Constants.AUTH_USER_PREFS_JSON, gson.toJson(user));
        editor.commit();
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

    public  static String getLoggedInPicture(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.AUTH_PREFS_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(Constants.AUTH_PICTURE_KEY, null);
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
}
