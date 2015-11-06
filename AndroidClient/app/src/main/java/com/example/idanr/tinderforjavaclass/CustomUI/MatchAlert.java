package com.example.idanr.tinderforjavaclass.CustomUI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.idanr.tinderforjavaclass.BusinessLogic.Login.LoginActivity;
import com.example.idanr.tinderforjavaclass.R;
import com.facebook.AccessToken;

/**
 * Created by idanr on 11/6/15.
 */
public class MatchAlert {
    public static AlertDialog instantiateAlert(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.customDialog);
        builder.setTitle("MATCH!!")
                .setMessage("WAY TO GO, KEEP ROLLING!");
//                .setPositiveButton("Keep Rolling", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                    }
//                });
        return builder.create();
    }
}
