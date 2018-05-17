package com.shikanga.innovest.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shikanga.innovest.R;
import com.shikanga.innovest.activity.MainActivity;
import com.shikanga.innovest.services.UserService;
import com.shikanga.innovest.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PasswordChangeFragment extends Fragment {
    View view;
    EditText oldPasswordView, newPasswordView, passwordConfirmView;
    Button submitButton;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_password_change, container, false);
        oldPasswordView = (EditText) view.findViewById(R.id.old_password);
        newPasswordView = (EditText) view.findViewById(R.id.new_password);
        passwordConfirmView = (EditText) view.findViewById(R.id.confirm_password);
        submitButton = (Button) view.findViewById(R.id.submitButton);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        return view;
    }

    private  Boolean changePassword()
    {
        String oldPassword, newPassword, confirmPassword;
        oldPassword = oldPasswordView.getText().toString();
        newPassword = newPasswordView.getText().toString();
        confirmPassword = passwordConfirmView.getText().toString();

        oldPasswordView.setError(null);
        newPasswordView.setError(null);
        passwordConfirmView.setError(null);

        if (TextUtils.isEmpty(oldPassword)){
            oldPasswordView.setError(getString(R.string.error_field_required));
            oldPasswordView.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(newPassword)){
            newPasswordView.setError(getString(R.string.error_field_required));
            newPasswordView.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(confirmPassword)){
            passwordConfirmView.setError(getString(R.string.error_field_required));
            passwordConfirmView.requestFocus();
            return false;
        }

        if ( !TextUtils.equals(newPassword, confirmPassword)){
            passwordConfirmView.setError(getString(R.string.error_password_mismatch));
            newPasswordView.setError(getString(R.string.error_password_mismatch));
            passwordConfirmView.requestFocus();
            return false;
        }

        if (!isPasswordValid(newPassword)){
            newPasswordView.setError(getString(R.string.error_invalid_password));
            newPasswordView.requestFocus();
            return false;
        }

        PasswordChangeAsync passwordChangeAsync = new PasswordChangeAsync(oldPassword, newPassword);
        passwordChangeAsync.execute();
        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private class PasswordChangeAsync extends AsyncTask<Void, Void, JSONObject>{
        private String oldPassword, newPassword;

        public PasswordChangeAsync(String oldPassword, String newPassword) {
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return UserService.changePassword(oldPassword, newPassword, getActivity());
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressBar.setVisibility(View.GONE);
            try{
                if (jsonObject != null){
                    if (jsonObject.has("status")){
                        Boolean success = jsonObject.getBoolean("status");
                        if (success){

                            Toast.makeText(getActivity(), getString(R.string.message_password_change_successful), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            String error = "";
                            JSONArray jsonArray = jsonObject.getJSONArray("old_password");
                            for (int i =0; i<jsonArray.length(); i++){
                                error += jsonArray.getString(i);
                            }
                            Snackbar.make(view, "Error : "+error, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        String error = "";
                        if (jsonObject.has("old_password")){
                            JSONArray jsonArray = jsonObject.getJSONArray("old_password");
                            for (int i =0; i<jsonArray.length(); i++){
                                error += jsonArray.getString(i);
                            }
                        }
                        if (jsonObject.has("new_password")){
                            JSONArray jsonArray = jsonObject.getJSONArray("new_password");
                            for (int i =0; i<jsonArray.length(); i++){
                                error += jsonArray.getString(i);
                            }
                        }
                        Snackbar.make(view, error, Snackbar.LENGTH_SHORT).show();
                    }

                }
                else {
                    Snackbar.make(view, getString(R.string.message_password_change_unsuccessful), Snackbar.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e){

            }
            super.onPostExecute(jsonObject);
        }
    }
}
