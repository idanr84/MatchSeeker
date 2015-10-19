package com.example.idanr.tinderforjavaclass;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.util.Log;

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


//        TextView customView = (TextView)
//                LayoutInflater.from(this).inflate(R.layout.actionbar_custom_title_view_centered,
//                        null);
//
//        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
//                ActionBar.LayoutParams.MATCH_PARENT,
//                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER );
//
//        customView.setText("Some centered text");
//        getSupportActionBar().setCustomView(customView, params);



        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton = (LoginButton) findViewById(R.id.login_button);
        mLoginButton.setReadPermissions("user_friends");
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent startLogin = new Intent(LoginActivity.this, UserMatchesActivity.class);
                LoginActivity.this.startActivity(startLogin);
            }

            @Override
            public void onCancel() {
                Log.d(TAG ,"onSuccess");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d(TAG ,"onSuccess");
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

