package com.shikanga.innovest.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shikanga.innovest.R;
import com.shikanga.innovest.activity.MainActivity;
import com.shikanga.innovest.models.Account;
import com.shikanga.innovest.models.User;
import com.shikanga.innovest.services.AccountService;
import com.shikanga.innovest.services.FilePathService;
import com.shikanga.innovest.services.UserService;
import com.shikanga.innovest.utils.Constants;
import com.shikanga.innovest.utils.ErrorUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.ConcurrentSkipListMap;

import static android.app.Activity.RESULT_OK;

public class UpdateAccountFragment extends Fragment {
    View view;
    Button choosePicture, submitButton;
    EditText firstNameView,lastNameView, telephoneView;
    private final  int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private final static int PICK_FILE_REQUEST =2;
    String profileImagePath;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_account, container, false);
        choosePicture = (Button) view.findViewById(R.id.button_choose_profile_picture);
        submitButton = (Button) view.findViewById(R.id.update_account_submit_button);
        firstNameView = (EditText) view.findViewById(R.id.first_name);
        lastNameView = (EditText) view.findViewById(R.id.last_name);
        telephoneView = (EditText) view.findViewById(R.id.telephone_number);

        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptUpdate();
            }
        });
        checkFilePermission();
        return view;
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_FILE_REQUEST :
                if (data==null){
                    return;
                }

                Uri selectedFileUri = data.getData();
                String filepath = FilePathService.getPath(getActivity(),selectedFileUri);
                choosePicture.setText(filepath);
                profileImagePath =filepath;

                break;
        }
    }

    private Boolean attemptUpdate(){
        String firstName, lastName, telephone;

        firstNameView.setError(null);
        lastNameView.setError(null);
        telephoneView.setError(null);

        firstName = firstNameView.getText().toString();
        lastName = lastNameView.getText().toString();
        telephone = telephoneView.getText().toString();

        if (TextUtils.isEmpty(telephone)){
            telephoneView.setError(getString(R.string.error_invalid_telephone));
            telephoneView.requestFocus();
            return false;
        }

        if (!isValidTelephone(telephone)){
            telephoneView.setError(getString(R.string.error_invalid_telephone));
            telephoneView.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(firstName)){
            firstNameView.setError(getString(R.string.error_field_required));
            firstNameView.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(lastName)){
            lastNameView.setError(getString(R.string.error_field_required));
            lastNameView.requestFocus();
            return false;
        }

        if (profileImagePath == null){
            Toast.makeText(getActivity(), "Profile Picture "+getString(R.string.error_field_required), Toast.LENGTH_SHORT).show();
            return false;
        }

        UpdateAccountTask accountTask = new UpdateAccountTask(firstName, lastName, telephone);
        accountTask.execute();
        return true;
    }

    private Boolean checkFilePermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view, getString(R.string.permission_request_read_ext_storage_), Snackbar.LENGTH_SHORT).show();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getActivity(), R.string.permission_result_success_read_ext_storage, Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), R.string.permission_result_fail_read_ext_storage, Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private Boolean isValidTelephone(String telephone){
        String substr = TextUtils.substring(telephone, 0, 3);
        if ( TextUtils.equals(substr, "2547")){
            return  false;
        }

        if (!TextUtils.isDigitsOnly(telephone)){
            return false;
        }

        if (TextUtils.getTrimmedLength(telephone) < 11){
            return false;
        }

        return true;
    }

    private class UpdateAccountTask extends AsyncTask<Void, Void, JSONObject>{
        String firstName, lastName, telephone;

        public UpdateAccountTask(String firstName, String lastName, String telephone) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.telephone = telephone;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            File imageFile = new File(profileImagePath);
            return AccountService.updateAccount(firstName, lastName, telephone, imageFile, getActivity());
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if ((jsonObject.toString().contains("[") && jsonObject.toString().contains("]") || jsonObject.has("detail"))){
                String [] fields = {"first_name", "last_name", "picture", "telephone", "detail"};
                String error = ErrorUtils.getErrorFromServerJSON(jsonObject, fields);
                Snackbar.make(view, error, Snackbar.LENGTH_SHORT).show();
            }
            else {
                User user = UserService.getLoggedInUser(getActivity());
                user = UserService.getUserById(user.getId());
                Account account = AccountService.getAccount(user.getId(), getActivity());
                UserService.setUserPref(getActivity(), user);
                UserService.setAccountPref(getActivity(), account);
                Toast.makeText(getActivity(), R.string.message_account_updated_successfully, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
            super.onPostExecute(jsonObject);
        }
    }
}
