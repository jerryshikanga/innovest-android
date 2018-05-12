package com.shikanga.innovest.adapters;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shikanga.innovest.R;
import com.shikanga.innovest.models.Campaign;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CampaignListAdapter extends RecyclerView.Adapter <CampaignListAdapter.ViewHolder> {
    private Campaign[] campaignList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTextView, summaryTextView;
        public ImageView pictureImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            summaryTextView = (TextView) itemView.findViewById(R.id.summaryTextView);
            pictureImageView = (ImageView) itemView.findViewById(R.id.pictureImageView);
        }
    }

    public CampaignListAdapter(Campaign[] campaignList) {
        this.campaignList = campaignList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.campaign_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Campaign campaign = campaignList[position];
        holder.nameTextView.setText(campaign.getName());
        holder.summaryTextView.setText(campaign.getSummary());
        Picasso.get().load(campaign.getPicture()).into(holder.pictureImageView);
    }

    @Override
    public int getItemCount() {
        if (campaignList == null){
            return 0;
        }
        return campaignList.length;
    }
}
