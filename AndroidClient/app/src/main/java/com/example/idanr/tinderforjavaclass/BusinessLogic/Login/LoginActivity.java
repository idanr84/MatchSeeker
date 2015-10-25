package com.example.idanr.tinderforjavaclass.BusinessLogic.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.idanr.tinderforjavaclass.Configuration.ConfigurationManager;
import com.example.idanr.tinderforjavaclass.BusinessLogic.PotentialMatches.PotentialMatchesActivity;
import com.example.idanr.tinderforjavaclass.Facebook.FacebookManager;
import com.example.idanr.tinderforjavaclass.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by idanr on 8/18/15.
 */
public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getName();

    CallbackManager mCallbackManager;
    private LoginButton mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton = (LoginButton) findViewById(R.id.login_button);
        mLoginButton.setReadPermissions("user_friends");
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                ConfigurationManager.sharedInstance().setIsConnectedToFacebook(true);
                FacebookManager.sharedInstance().fetchUserInfo();
                Intent startLogin = new Intent(LoginActivity.this, PotentialMatchesActivity.class);
                LoginActivity.this.startActivity(startLogin);
            }

            @Override
            public void onCancel() {
                ConfigurationManager.sharedInstance().setIsConnectedToFacebook(false);
            }

            @Override
            public void onError(FacebookException e) {
                ConfigurationManager.sharedInstance().setIsConnectedToFacebook(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }
}

