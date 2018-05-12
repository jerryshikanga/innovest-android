package com.shikanga.innovest.services;

import com.google.gson.Gson;
import com.shikanga.innovest.models.Category;
import com.shikanga.innovest.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CategoryService {
    public static Category[] getListCategory(){
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(Constants.CATEGORY_PATH).build();
            Response response = client.newCall(request).execute();
            JSONArray serverJSON = new JSONArray(response.body().string());

            Gson gson = new Gson();
            Category[] categoriesArray =  gson.fromJson(serverJSON.toString(), Category[].class);
            return categoriesArray;
        }
        catch (IOException e){

        }
        catch (JSONException e){

        }
        return null;
    }
}
