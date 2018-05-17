package com.shikanga.innovest.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shikanga.innovest.R;
import com.shikanga.innovest.activity.MainActivity;
import com.shikanga.innovest.models.Campaign;
import com.shikanga.innovest.models.Category;
import com.shikanga.innovest.services.CampaignListService;
import com.shikanga.innovest.services.CategoryService;
import com.shikanga.innovest.services.FilePathService;
import com.shikanga.innovest.services.UserService;
import com.shikanga.innovest.utils.Constants;
import com.shikanga.innovest.utils.ErrorUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CampaignNewFragment extends Fragment {
    private View view;
    private Spinner categorySpinner;
    private EditText nameView, summaryView, descriptionView, amountView;
    private DatePicker dateEndView;
    private Button submitButton, campaignPhotoBtn;
    private ProgressBar progressBar;
    private static final int CAMPAIGN_PHOTO_REQUEST_CODE = 19;
    private String campaignPhotoPath = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.campaign_new_layout, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        categorySpinner = (Spinner) view.findViewById(R.id.category_spinner);
        nameView = (EditText) view.findViewById(R.id.name);
        summaryView = (EditText) view.findViewById(R.id.summary);
        descriptionView = (EditText) view.findViewById(R.id.description);
        amountView = (EditText) view.findViewById(R.id.amount);
        dateEndView = (DatePicker) view.findViewById(R.id.date_end);
        submitButton = (Button) view.findViewById(R.id.submit);
        campaignPhotoBtn = (Button) view.findViewById(R.id.button_new_campaign_photo);

        categorySpinner.setPrompt(getString(R.string.hint_new_campaign_category));
        Date date = new Date();
        dateEndView.setMinDate(date.getTime());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        campaignPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoChooser();
            }
        });

        CategoryTask categoryTask = new CategoryTask();
        categoryTask.execute();

        return view;
    }

    private void openPhotoChooser(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, CAMPAIGN_PHOTO_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMPAIGN_PHOTO_REQUEST_CODE :
                if (data.getData() != null ){
                    Uri uri = data.getData();
                    campaignPhotoPath = FilePathService.getPath(getActivity(), uri);
                    campaignPhotoBtn.setText(campaignPhotoPath);
                }
        }
    }

    private  Boolean attemptRegister()
    {
        String name, summary, description;
        Float amount;
        Date endDate;
        Category category;

        nameView.setError(null);
        summaryView.setError(null);
        descriptionView.setError(null);
        amountView.setError(null);

        name = nameView.getText().toString();
        summary = summaryView.getText().toString();
        description = descriptionView.getText().toString();
        amount = Float.parseFloat(amountView.getText().toString());
        endDate = new Date(dateEndView.getDayOfMonth(), dateEndView.getMonth(), dateEndView.getYear());
        category = (Category) categorySpinner.getSelectedItem();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name) ){
            cancel = true;
            focusView = nameView;
            nameView.setError(getString(R.string.error_field_required));
        }

        if (TextUtils.isEmpty(summary)){
            cancel =true;
            focusView = summaryView;
            summaryView.setError(getString(R.string.error_field_required));
        }

        if (TextUtils.isEmpty(description)){
            cancel = true;
            focusView = descriptionView;
            descriptionView.setError(getString(R.string.error_field_required));
        }

        if (TextUtils.isEmpty(String.valueOf(amount))){
            cancel = true;
            focusView = amountView;
            amountView.setError(getString(R.string.error_field_required));
        }

        if (amount <= 0 ){
            cancel = true;
            focusView = amountView;
            amountView.setError(getString(R.string.error_amount_invalid));
        }

        if (campaignPhotoPath == null || TextUtils.isEmpty(campaignPhotoPath)){
            Snackbar.make(view, "Campaign Photo "+getString(R.string.error_field_required), Snackbar.LENGTH_SHORT).show();
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
            return false;
        }
        else {
            CampaignNewTask campaignNewTask = new CampaignNewTask(name, summary, description, amount, endDate, category, campaignPhotoPath);
            campaignNewTask.execute();
            return  true;
        }

    }

    private void populateCategorySpinner(Category [] categories) {
        if (categories != null){
            ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<Category>(getActivity(), android.R.layout.simple_spinner_item, categories);
            categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(categoryArrayAdapter);
        }

    }

    public class CategoryTask extends AsyncTask<Void, Void, Category[]>
    {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Category[] doInBackground(Void... voids) {
            Category[] categories = CategoryService.getListCategory();
            return categories;
        }

        @Override
        protected void onPostExecute(Category[] categories) {
            progressBar.setVisibility(View.GONE);
            populateCategorySpinner(categories);
        }
    }

    private class CampaignNewTask extends AsyncTask<Void, Void, JSONObject>{
        private String name, summary, description;
        private Float amount;
        private Date endDate;
        private Category category;
        private String imagePath;

        public CampaignNewTask(String name, String summary, String description, Float amount, Date endDate, Category category, String imagePath) {
            this.name = name;
            this.summary = summary;
            this.description = description;
            this.amount = amount;
            this.endDate = endDate;
            this.category = category;
            this.imagePath = imagePath;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            File file = new File(imagePath);
            JSONObject result = CampaignListService.registerCampaign(getActivity(), name, summary, description, amount, endDate, category, file);
            return  result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            progressBar.setVisibility(View.GONE);
            if (result != null){
                if ((result.toString().contains("[") && result.toString().contains("]") || result.has("detail"))){
                    String [] fields = {"name", "summary", "description", "picture", "end", "amount", "category"};
                    String error = ErrorUtils.getErrorFromServerJSON(result, fields);
                    Snackbar.make(view, error, Snackbar.LENGTH_SHORT).show();
                }
                else{
                    Gson gson = new Gson();
                    Campaign campaign = gson.fromJson(result.toString(), Campaign.class);
                    if (campaign.getId() > 0){
                        Toast.makeText(getActivity(), getString(R.string.message_campaign_registered_successfully), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
            else {
                Snackbar.make(view, getString(R.string.message_failed_register_campaign), Snackbar.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

}
