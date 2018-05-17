package com.shikanga.innovest.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shikanga.innovest.R;
import com.shikanga.innovest.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private View view;
    private TextView userCount, campaignCount, fundCount;
    private ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_home_fragment, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        userCount = view.findViewById(R.id.users_count);
        campaignCount = view.findViewById(R.id.campaigns_count);
        fundCount = view.findViewById(R.id.funds_count);
        StatsAsync async = new StatsAsync();
        async.execute();
        return view;
    }

    private class StatsAsync extends AsyncTask<Void, Void, JSONObject>{
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            try{
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Constants.STATISTICS_URL).build();
                Response response = client.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                return jsonObject;
            }
            catch (IOException e){

            }
            catch (JSONException e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            progressBar.setVisibility(View.GONE);
            try {
                if (jsonObject!= null){
                    userCount.append(jsonObject.getString("user_count"));
                    campaignCount.append(jsonObject.getString("campaign_count"));
                    fundCount.append(jsonObject.getString("bid_total"));
                }
            }
            catch (JSONException e){

            }
        }
    }
}
