package com.shikanga.innovest.fragments;

import android.app.Fragment;
import android.content.Context;
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
import android.widget.Toast;

import com.shikanga.innovest.R;
import com.shikanga.innovest.adapters.BidAdapter;
import com.shikanga.innovest.models.Bid;
import com.shikanga.innovest.services.BidService;
import com.shikanga.innovest.utils.Constants;

public class BidFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bid_list_fragment_layout, container, false);
        recyclerView = view.findViewById(R.id.bid_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        BidTask bidTask = new BidTask();
        bidTask.execute();
        return view;
    }

    private  void updateView(Bid[] bids){
        if (bids != null ){
            BidAdapter adapter = new BidAdapter(bids);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(getContext(), getString(R.string.error_zero_bids), Toast.LENGTH_SHORT).show();
        }
    }

    private  class BidTask extends AsyncTask<Void, Void, Bid[]>{
        @Override
        protected Bid[] doInBackground(Void... voids) {
            Bid[] bids = BidService.getListBid(getContext());
            return bids;
        }

        @Override
        protected void onPostExecute(Bid[] bids) {
            updateView(bids);
        }
    }

    @Override
    public Context getContext() {
        return  getActivity();
    }
}
