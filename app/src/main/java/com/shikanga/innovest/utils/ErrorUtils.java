package com.shikanga.innovest.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ErrorUtils {
    public static String getErrorFromServerJSON(JSONObject jsonObject, String [] fields){
        fields[fields.length+1] = "detail";
        String error = "";
        try{
            for (String field : fields){
                if (TextUtils.equals(field, "detail")){
                    error += jsonObject.getString(field);
                    continue;
                }
                if (jsonObject.has(field)){
                    JSONArray jsonArray = jsonObject.getJSONArray(field);
                    for (int i = 0; i<jsonArray.length(); i++){
                        error += jsonArray.getString(i);
                    }
                }
            }
        }
        catch (JSONException e){

        }
        return error;
    }

}
