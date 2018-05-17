package com.shikanga.innovest.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shikanga.innovest.R;
import com.shikanga.innovest.models.Category;
import com.shikanga.innovest.utils.Constants;
import com.squareup.picasso.Picasso;

public class CategoryAdapter extends RecyclerView.Adapter <CategoryAdapter.ViewHolder> {
    private Category[] categoriesDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTextView;
        public ImageView pictureImageView;
        private TextView summaryTextView;

        public ViewHolder(View view){
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            summaryTextView = view.findViewById(R.id.summaryTextView);
            pictureImageView = view.findViewById(R.id.logoImageView);


        }
    }

    public CategoryAdapter(Category[] categoriesDataSet) {
        this.categoriesDataSet = categoriesDataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoriesDataSet[position];
        Picasso.get().load(category.getPicture()).into(holder.pictureImageView);
        holder.summaryTextView.setText(category.getSummary());
        holder.nameTextView.setText(category.getName());
    }



    @Override
    public int getItemCount() {
        if (categoriesDataSet != null){
            return categoriesDataSet.length;
        }
        return 0;
    }
}