package com.shikanga.innovest.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shikanga.innovest.R;
import com.shikanga.innovest.adapters.CampaignListAdapter;
import com.shikanga.innovest.models.Campaign;
import com.shikanga.innovest.models.Category;
import com.shikanga.innovest.services.CampaignListService;
import com.shikanga.innovest.utils.Constants;

public class FragmentAuthUserCampaigns extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private Category category;
    private TextView categoriesView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.campaign_list_fragment_layout, container, false);
        categoriesView = view.findViewById(R.id.category_name);


        CampaignTask task = new CampaignTask(category);
        task.execute();
        return view;
    }

    public void updateView(final Campaign[] campaignList){
        recyclerView = (RecyclerView) view.findViewById(R.id.company_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoriesView.setText("My campaigns");
        if (campaignList != null){
            CampaignListAdapter adapter = new CampaignListAdapter(campaignList);
            recyclerView.setAdapter(adapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            adapter.notifyDataSetChanged();
        }
        else{
            Snackbar.make(view, getString(R.string.error_zero_campaigns), Snackbar.LENGTH_SHORT).show();
        }
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());
                if (childView!=null && gestureDetector.onTouchEvent(e)){
                    int position = rv.getChildAdapterPosition(childView);
                    Campaign campaign = campaignList[position];
                    CampaignDetailFragment fragment = new CampaignDetailFragment();
                    Bundle arguments = new Bundle();
                    Gson gson = new Gson();
                    arguments.putString("campaign", gson.toJson(campaign));
                    fragment.setArguments(arguments);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.commit();
                }
                return  false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    private class CampaignTask extends AsyncTask <Void, Void, Campaign[]>{
        private  Category category;

        public CampaignTask(Category category) {
            this.category = category;
        }

        @Override
        protected Campaign[] doInBackground(Void... params) {
            Campaign[] campaignList = CampaignListService.getLoggedInUserCampaigns(getActivity());
            return campaignList;
        }

        @Override
        protected void onPostExecute(Campaign[] campaigns) {
            updateView(campaigns);
            super.onPostExecute(campaigns);
        }
    }
}
