package com.shikanga.innovest.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shikanga.innovest.R;
import com.shikanga.innovest.adapters.CampaignListAdapter;
import com.shikanga.innovest.models.Campaign;
import com.shikanga.innovest.services.CampaignListService;
import com.shikanga.innovest.utils.Constants;

import java.util.List;

public class CampaignListFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private int categoryId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.campaign_list_fragment_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.company_list_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        CampaignTask task = new CampaignTask();
        task.execute();
        return view;
    }

    @Override
    public void setArguments(Bundle args) {
        categoryId = args.getInt("categoryId");
        super.setArguments(args);
    }

    public void updateView(Campaign[] campaignList){
        if (campaignList.length>0){
            CampaignListAdapter adapter = new CampaignListAdapter(campaignList);
            recyclerView.setAdapter(adapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            adapter.notifyDataSetChanged();
        }
        else{
            Snackbar.make(view, getString(R.string.error_zero_campaigns), Snackbar.LENGTH_SHORT).show();
        }

    }

    private class CampaignTask extends AsyncTask <Void, Void, Campaign[]>{
        @Override
        protected Campaign[] doInBackground(Void... params) {
            Campaign[] campaignList = CampaignListService.getListCampaign();
            return campaignList;
        }

        @Override
        protected void onPostExecute(Campaign[] campaigns) {
            updateView(campaigns);
            super.onPostExecute(campaigns);
        }
    }
}
