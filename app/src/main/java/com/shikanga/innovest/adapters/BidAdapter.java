package com.shikanga.innovest.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shikanga.innovest.R;
import com.shikanga.innovest.models.Bid;
import com.shikanga.innovest.utils.Constants;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.BidViewHolder> {
    private Bid[] bids;

    public static  class BidViewHolder extends  RecyclerView.ViewHolder{
        private TextView amountView, campaignView, dateView;
        public BidViewHolder(View itemView) {
            super(itemView);
            amountView = (TextView) itemView.findViewById(R.id.amount_view);
            campaignView = (TextView) itemView.findViewById(R.id.campaign_view);
            dateView = (TextView) itemView.findViewById(R.id.date_view);

        }
    }

    public BidAdapter(Bid[] bids) {
        this.bids = bids;
    }

    @NonNull
    @Override
    public BidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bid_list_row, parent, false);
        return new BidViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BidViewHolder holder, int position) {
        Bid bid = bids[position];
        holder.amountView.setText(String.valueOf(bid.getAmount()));
        holder.dateView.setText(bid.getDate().toString());
        holder.campaignView.setText(bid.getCampaign());
    }

    @Override
    public int getItemCount() {
        if (bids == null){
            return 0;
        }
        return bids.length;
    }
}
