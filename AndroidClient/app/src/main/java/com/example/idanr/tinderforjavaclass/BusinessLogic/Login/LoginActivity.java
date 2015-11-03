package com.example.idanr.tinderforjavaclass.BusinessLogic.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.idanr.tinderforjavaclass.BusinessLogic.PotentialMatches.PotentialMatchesActivity;
import com.example.idanr.tinderforjavaclass.Configuration.ConfigurationManager;
import com.example.idanr.tinderforjavaclass.Facebook.FacebookHelper;
import com.example.idanr.tinderforjavaclass.Model.CurrentUser;
import com.example.idanr.tinderforjavaclass.NetworkManager.NetworkClient;
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
    private NetworkClient mNetworkClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mNetworkClient = new NetworkClient(new NetworkClient.LoginListener() {
            @Override
            public void loginSuccessed(String accessToken, String userID) {
                mNetworkClient.getUserInfo();
            }

            @Override
            public void loginFailed(String error) {

            }

            @Override
            public void userInfoFetched(CurrentUser currentUser) {
                ConfigurationManager.sharedInstance().setCurrentUser(currentUser);
                Intent startMatchingProcess = new Intent(LoginActivity.this, PotentialMatchesActivity.class);
                LoginActivity.this.startActivity(startMatchingProcess);
                finish();
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton = (LoginButton) findViewById(R.id.login_button);
        mLoginButton.setReadPermissions("user_photos","user_location","user_friends","user_birthday","user_likes");
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                ConfigurationManager.sharedInstance().setIsConnectedToFacebook(true);

                new FacebookHelper(new FacebookHelper.FacebookLoginListener() {
                    @Override
                    public void fetchedFacebookInfoSuccess(String facebookToken, String facebookID) {
                        mNetworkClient.login(facebookToken, facebookID);

                    }

                    @Override
                    public void fetchedFacebookInfoFailure(String error) {

                    }
                }).fetchUserInfo();
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

