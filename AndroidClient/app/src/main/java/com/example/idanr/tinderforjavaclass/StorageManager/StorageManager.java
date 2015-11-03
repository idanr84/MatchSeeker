package com.example.idanr.tinderforjavaclass.StorageManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.idanr.tinderforjavaclass.Configuration.ConfigurationManager;
import com.example.idanr.tinderforjavaclass.Model.BaseUser;
import com.example.idanr.tinderforjavaclass.Model.CurrentUser;
import com.example.idanr.tinderforjavaclass.Model.PotentialMatch;
import com.example.idanr.tinderforjavaclass.R;

import java.util.ArrayList;

/**
 * Created by idanr on 10/28/15.
 */
public class StorageManager {

    static StorageManager mSharedInstace;
    private Context mContext;
    private CurrentUser mCurrentUser;
    private ArrayList<PotentialMatch> mPotentialMatches;

    public static StorageManager sharedInstance() {
        if (mSharedInstace == null) {
            mSharedInstace = new StorageManager();
        }
        return mSharedInstace;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void initWithContext(Context context){
        mContext = context;
        refreshData();
//        createTestPotentialMatches();
    }

    public void refreshData(){
        mCurrentUser = ConfigurationManager.sharedInstance().getCurrentUser();
        if (mCurrentUser != null) {
            mPotentialMatches = mCurrentUser.getPotentialMatches();
        }
    }

    public ArrayList<PotentialMatch> returnTestPotentialMatches() {
        Bitmap testBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.linda);
        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://scontent.xx.fbcdn.net/hphotos-xpa1/v/t1.0-0/p130x130/10393773_10153213180429219_3744986111755366759_n.jpg?oh=8761c45c5f18d30150c201663cfe0634&oe=56B2AC15");
        urls.add("https://scontent.xx.fbcdn.net/hphotos-xpa1/v/t1.0-0/p130x130/10393773_10153213180429219_3744986111755366759_n.jpg?oh=8761c45c5f18d30150c201663cfe0634&oe=56B2AC15");
        urls.add("https://scontent.xx.fbcdn.net/hphotos-xpa1/v/t1.0-0/p130x130/10393773_10153213180429219_3744986111755366759_n.jpg?oh=8761c45c5f18d30150c201663cfe0634&oe=56B2AC15");
        urls.add("https://scontent.xx.fbcdn.net/hphotos-xpa1/v/t1.0-0/p130x130/10393773_10153213180429219_3744986111755366759_n.jpg?oh=8761c45c5f18d30150c201663cfe0634&oe=56B2AC15");
        urls.add("https://scontent.xx.fbcdn.net/hphotos-xpa1/v/t1.0-0/p130x130/10393773_10153213180429219_3744986111755366759_n.jpg?oh=8761c45c5f18d30150c201663cfe0634&oe=56B2AC15");

        ArrayList<PotentialMatch> al = new ArrayList<PotentialMatch>();
        al.add(new PotentialMatch("idan 0","31","4",urls,"liked"));
        al.add(new PotentialMatch("idan 1 ","31","4",urls,"liked"));
        al.add(new PotentialMatch("idan 2 ","31","4",urls,"liked"));
        al.add(new PotentialMatch("idan 3","31","4",urls,"liked"));
        return  al;
    }

    public int getNumberOfPotentialMatches(){
        return mPotentialMatches.size();
    }

    public PotentialMatch getPotentialMatchAtIndex(int position){
        return mPotentialMatches.get(position);
    }

    public ArrayList<PotentialMatch> getPotentialMatches(){
        return mPotentialMatches;
    }


}
