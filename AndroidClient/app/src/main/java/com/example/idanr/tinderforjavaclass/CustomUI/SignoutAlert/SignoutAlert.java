package com.example.idanr.tinderforjavaclass.CustomUI.SignoutAlert;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.idanr.tinderforjavaclass.BusinessLogic.Login.LoginActivity;
import com.example.idanr.tinderforjavaclass.Configuration.ConfigurationManager;
import com.example.idanr.tinderforjavaclass.Facebook.FacebookHelper;
import com.facebook.AccessToken;

/**
 * Created by idanr on 11/3/15.
 */
public class SignoutAlert extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to sign out?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent startLogin = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivity(startLogin);
                        getActivity().finish();
                        FacebookHelper.setAccessToken(null);
                        ConfigurationManager.sharedInstance().signOut();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        return builder.create();
    }

}
