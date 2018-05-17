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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shikanga.innovest.R;
import com.shikanga.innovest.activity.MainActivity;
import com.shikanga.innovest.adapters.CategoryAdapter;
import com.shikanga.innovest.models.Category;
import com.shikanga.innovest.services.CategoryService;

public class CategoryFragment extends Fragment {
    View view;
    RecyclerView recyclerView;

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

    public Boolean updateView(final Category [] categories)
    {

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        CategoryAdapter adapter = new CategoryAdapter(categories);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return  true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)){
                    int position = rv.getChildAdapterPosition(child);
                    Category category = categories[position];
                    CampaignListFragment campaignListFragment = new CampaignListFragment();
                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    bundle.putString("category", gson.toJson(category));
                    campaignListFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, campaignListFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
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
