package com.shikanga.innovest.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shikanga.innovest.R;
import com.shikanga.innovest.models.Campaign;
import com.shikanga.innovest.services.BidService;
import com.shikanga.innovest.services.UserService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CampaignDetailFragment extends Fragment {
    private View view;
    private Campaign campaign = null;
    private TextView campaignName, campaignAmount, campaignDescription;
    private ImageView camapignImage;
    private Button submitNewBidButton, editCampaignBtn;
    private EditText amountNewBidView;

    Gson gson = new Gson();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            String campaignJson = getArguments().getString("campaign", null);
            if (campaignJson != null){
                campaign = gson.fromJson(campaignJson, Campaign.class);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.campaign_detail, container, false);
        campaignName = (TextView) view.findViewById(R.id.campaign_name);
        campaignAmount = (TextView) view.findViewById(R.id.campaign_amount);
        campaignDescription =(TextView) view.findViewById(R.id.campaign_description);
        camapignImage = (ImageView) view.findViewById(R.id.campaign_image);
        submitNewBidButton = (Button) view.findViewById(R.id.submit_new_bid);
        amountNewBidView = (EditText) view.findViewById(R.id.amount_new_bid);
        editCampaignBtn = (Button) view.findViewById(R.id.edit_campaign_button);

        submitNewBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptBid();
            }
        });

        editCampaignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentCampaignUpdate fragment = new FragmentCampaignUpdate();
                Bundle bundle = new Bundle();
                if (campaign!=null){
                    Gson gson = new Gson();
                    bundle.putString("campaign", gson.toJson(campaign));
                }
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });

        if (campaign!=null){
            if (campaign.getUser() == UserService.getLoggedInUser(getActivity()).getId()){
                editCampaignBtn.setVisibility(View.VISIBLE);
            }

            campaignName.setText(campaign.getName());
            campaignAmount.setText(String.valueOf(campaign.getAmount()));
            campaignDescription.setText(campaign.getDescription());

            Picasso.get().load(campaign.getPicture()).into(camapignImage);
        }
        else{
            Snackbar.make(view, getString(R.string.error_campaign_invalid), Snackbar.LENGTH_SHORT).show();
        }
        return view;
    }

    public  void loadFragment(Fragment fragment){
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(currentFragment != null && fragment.getTag()!=null){
            if (currentFragment.getTag().equals(fragment.getTag())){
                return;
            }
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private Boolean attemptBid()
    {
        amountNewBidView.setError(null);
        int amount;
        if (!TextUtils.isDigitsOnly(amountNewBidView.getText())){
            amountNewBidView.setError(getString(R.string.error_amount_invalid));
            amountNewBidView.requestFocus();
            return false;
        }
        amount = Integer.parseInt(amountNewBidView.getText().toString());
        if (!isAmountValid(amount)){
            amountNewBidView.setError(getString(R.string.error_amount_invalid));
            amountNewBidView.requestFocus();
            return false;
        }
        BidNewTask bidNewTask = new BidNewTask(amount, campaign);
        bidNewTask.execute();
        return true;
    }

    private Boolean isAmountValid(float amount){
        return amount > 0;
    }

    private class BidNewTask extends AsyncTask<Void, Void, JSONObject>{
        private int amount;
        private Campaign campaign;

        public BidNewTask(int amount, Campaign campaign) {
            this.amount = amount;
            this.campaign = campaign;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return BidService.placeBid(amount, campaign, getActivity());
        }

        @Override
        protected void onPostExecute(JSONObject serverJSON) {
            try{
                if (serverJSON.has("status")){
                    Boolean success = serverJSON.getBoolean("status");
                    if (success){
                        Snackbar.make(view, getString(R.string.message_bid_placed_successfully), Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        if (serverJSON.has("message")){
                            String message = serverJSON.getString("message");
                            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
                        }
                        else {
                            Snackbar.make(view, getString(R.string.message_failed_to_place_bid), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
                else if (serverJSON.has("amount")){
                    String error = "";
                    JSONArray jsonArray = serverJSON.getJSONArray("amount");
                    for (int i=0; i<jsonArray.length(); i++){
                        error += jsonArray.getString(i);
                    }
                    Snackbar.make(view, error, Snackbar.LENGTH_SHORT).show();
                }
                else if(serverJSON.has("campaign")){
                    String error = "";
                    JSONArray jsonArray = serverJSON.getJSONArray("campaign");
                    for (int i=0; i<jsonArray.length(); i++){
                        error += jsonArray.getString(i);
                    }
                    Snackbar.make(view, error, Snackbar.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e){
                Snackbar.make(view, getString(R.string.message_failed_to_place_bid), Snackbar.LENGTH_SHORT).show();
            }
            super.onPostExecute(serverJSON);
        }
    }
}
