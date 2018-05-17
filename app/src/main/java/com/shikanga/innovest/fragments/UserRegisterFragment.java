package com.shikanga.innovest.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shikanga.innovest.LoginActivity;
import com.shikanga.innovest.R;
import com.shikanga.innovest.activity.MainActivity;
import com.shikanga.innovest.models.User;
import com.shikanga.innovest.services.UserService;
import com.shikanga.innovest.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;

public class UserRegisterFragment extends Fragment {
    View view;
    private Button registerButton, loginButton;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView, mPasscordConfirmView, mFirstNameView, mLastNameView;

    private View mProgressView;
    private View mRegisterFormView;
    /**
     * Keep track of the register task to ensure we can cancel it if requested.
     */
    private UserRegisterTask mAuthTask = null;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.user_register_fragment_layout, container, false);
        mRegisterFormView = view.findViewById(R.id.user_register_form);
        mProgressView = view.findViewById(R.id.register_progress);
        registerButton = (Button) view.findViewById(R.id.email_sign_up_button);
        loginButton = (Button) view.findViewById(R.id.login_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new LoginFragment());
            }
        });


        // Set up the register form.
        mEmailView = view.findViewById(R.id.email);

        mPasswordView = view.findViewById(R.id.password);
        mPasscordConfirmView = view.findViewById(R.id.password_confirm);
        mFirstNameView = view.findViewById(R.id.first_name);
        mLastNameView = view.findViewById(R.id.last_name);
        mPasscordConfirmView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });
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

    /**
     * Attempts to sign in or register the account specified by the register form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual register attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasscordConfirmView.setError(null);
        mFirstNameView.setError(null);
        mLastNameView.setError(null);

        // Store values at the time of the register attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordConfirm = mPasscordConfirmView.getText().toString();
        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordConfirm) && !isPasswordValid(passwordConfirm)) {
            mPasscordConfirmView.setError(getString(R.string.error_invalid_password));
            focusView = mPasscordConfirmView;
            cancel = true;
        }

        //check if password and password confirm match
        if (!TextUtils.equals(password, passwordConfirm)){
            mPasswordView.setError(getString(R.string.error_password_mismatch));
            mPasscordConfirmView.setError(getString(R.string.error_password_mismatch));
            focusView = mPasscordConfirmView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(firstName)){
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(lastName)){
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user register attempt.
            showProgress(true);
            mAuthTask = new UserRegisterTask(email, password, passwordConfirm, firstName, lastName);
            mAuthTask.execute((Void) null);
        }
    }



    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    /**
     * Shows the progress UI and hides the register form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, User> {

        private final String mEmail;
        private final String mPassword;
        private final String mPasswordConfirm;
        private final String mFirstName;
        private final String mLastName;

        public UserRegisterTask(String mEmail, String mPassword, String mPasswordConfirm, String mFirstName, String mLastName) {
            this.mEmail = mEmail;
            this.mPassword = mPassword;
            this.mPasswordConfirm = mPasswordConfirm;
            this.mFirstName = mFirstName;
            this.mLastName = mLastName;
        }

        @Override
        protected User doInBackground(Void... params) {
            try{
                JSONObject serverResponse =  UserService.registerUser(mEmail, mEmail, mPassword, mPasswordConfirm, mFirstName, mLastName);
                if (serverResponse != null){

                    if (! serverResponse.toString().contains("[") ){
                        Log.i(Constants.CUSTOM_LOG_TAG, "In success block : ");
                        Gson gson = new Gson();
                        User user = gson.fromJson(serverResponse.toString(), User.class);
                        return user;
                    }
                    else {
                        String error = "";
                        if (serverResponse.has("username")){

                            JSONArray array = serverResponse.getJSONArray("username");

                            for (int i=0; i<array.length(); i++){
                                error += array.getString(i);
                            }
                        }
                        if (serverResponse.has("email")){
                            JSONArray array = serverResponse.getJSONArray("email");

                            for (int i=0; i<array.length(); i++){
                                error += array.getString(i);
                            }
                        }
                        if (serverResponse.has("password1")){
                            JSONArray array = serverResponse.getJSONArray("password1");

                            for (int i=0; i<array.length(); i++){
                                error += array.getString(i);
                            }
                        }
                        if (serverResponse.has("password2")){
                            JSONArray array = serverResponse.getJSONArray("password2");
                            for (int i=0; i<array.length(); i++){
                                error += array.getString(i);
                            }
                        }
                        if (serverResponse.has("password")){
                            JSONArray array = serverResponse.getJSONArray("password");

                            for (int i=0; i<array.length(); i++){
                                error += array.getString(i);
                            }
                        }

                        Snackbar.make(view, "Error "+error, Snackbar.LENGTH_SHORT).show();

                    }

                }
            }
            catch (JSONException e){
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(final User user) {
            mAuthTask = null;
            showProgress(false);

            if (user != null) {
                Toast.makeText(getActivity(), "Welcome "+user.getFullName()+".  "+getString(R.string.notification_register_success), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.notification_register_failure), Toast.LENGTH_SHORT).show();
                mEmailView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
