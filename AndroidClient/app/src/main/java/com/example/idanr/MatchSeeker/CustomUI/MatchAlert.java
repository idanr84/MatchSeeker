package com.example.idanr.MatchSeeker.CustomUI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import com.example.idanr.MatchSeeker.R;


/**
 * Created by idanr on 11/6/15.
 */
public class MatchAlert {
    public static void showAlert(Context context, String title, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.customDialog);
        builder.setTitle(title)
                .setMessage(msg);

        final Handler handler  = new Handler();
        final AlertDialog alert = builder.create();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();
                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog){

            }

        });

        alert.show();
        handler.postDelayed(runnable, 2000);
    }
}
