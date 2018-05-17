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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shikanga.innovest.R;
import com.shikanga.innovest.services.UserService;

public class PasswordResetFragment extends Fragment {
    private View view;
    private Button signInBtn, resetPwdButn;
    private EditText emailView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_password_reset, container, false);
        signInBtn = (Button) view.findViewById(R.id.sign_in_button);
        resetPwdButn = (Button) view.findViewById(R.id.password_reset_submit_button);
        emailView = (EditText) view.findViewById(R.id.email_address);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new LoginFragment());
            }
        });

        resetPwdButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptPasswordReset();
            }
        });

        return view;
    }

    private Boolean attemptPasswordReset(){
        String email = emailView.getText().toString();
        if (TextUtils.isEmpty(email)){
            emailView.setError(getString(R.string.error_field_required));
            return false;
        }

        if (!isEmailValid(email)){
            emailView.setError(getString(R.string.error_invalid_email));
            emailView.requestFocus();
            return false;
        }
        PasswordResetTask task = new PasswordResetTask(email);
        task.execute();
        return  true;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
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

    class PasswordResetTask extends AsyncTask<Void, Void, Boolean>{
        private String email;

        public PasswordResetTask(String email) {
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return UserService.resetPassword(email);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(success);
            if (success){
                Toast.makeText(getActivity(), getString(R.string.password_reset_successful), Toast.LENGTH_SHORT).show();
                loadFragment(new LoginFragment());
            }
            else{
                Snackbar.make(view, R.string.password_reset_unsuccessful, Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
