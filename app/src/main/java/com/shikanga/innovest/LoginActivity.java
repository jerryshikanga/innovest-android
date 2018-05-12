




package com.shikanga.innovest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.shikanga.innovest.fragments.LoginFragment;
import com.shikanga.innovest.activity.MainActivity;
import com.shikanga.innovest.fragments.UserRegisterFragment;
import com.shikanga.innovest.services.UserService;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(UserService.getLoggedInAuthToken(getApplicationContext()) != null){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        Button registerIntentButton = (Button) findViewById(R.id.registerIntentButton);
        registerIntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new UserRegisterFragment());
            }
        });

        loadFragment(new LoginFragment());
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }


}

