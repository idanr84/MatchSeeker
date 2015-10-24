package com.example.idanr.tinderforjavaclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.idanr.tinderforjavaclass.PotentialMatches.PotentialMatchesActivity;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AccessToken token = AccessToken.getCurrentAccessToken();

        Intent matchedActivity = new Intent(MainActivity.this, PotentialMatchesActivity.class);
        startActivity(matchedActivity );

//        if (token != null){
//            Intent matchedActivity = new Intent(MainActivity.this, PotentialMatchesActivity.class);
//            startActivity(matchedActivity );
//        } else {
//            Intent startLogin = new Intent(this, LoginActivity.class);
//            this.startActivity(startLogin);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
