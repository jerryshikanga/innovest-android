package com.shikanga.innovest.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shikanga.innovest.R;
import com.shikanga.innovest.adapters.CategoryAdapter;
import com.shikanga.innovest.models.Category;
import com.shikanga.innovest.services.CategoryService;

public class CategoryFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.category_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.category_recycler_view);
        recyclerView.setHasFixedSize(true);
        CategoryTask task = new CategoryTask();
        task.execute();
        return view;
    }

    public Boolean updateView(Category [] categories)
    {
        recyclerView.setLayoutManager(layoutManager);
        CategoryAdapter adapter = new CategoryAdapter(categories);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
        return true;
    }

    public class CategoryTask extends AsyncTask<Void, Void, Category[]>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Category[] doInBackground(Void... voids) {
            Category[] categories = CategoryService.getListCategory();
            return categories;
        }

        @Override
        protected void onPostExecute(Category[] categories) {
            updateView(categories);
        }
    }

}
