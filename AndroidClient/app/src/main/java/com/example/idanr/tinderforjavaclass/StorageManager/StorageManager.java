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

    private void refreshData(){
        mCurrentUser = ConfigurationManager.sharedInstance().getCurrentUser();
        if (mCurrentUser != null) {
            mPotentialMatches = mCurrentUser.getPotentialMatches();
        }
    }

    private void createTestPotentialMatches() {
        Bitmap testBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.linda);
        ArrayList<String> urls = new ArrayList<>();
        urls.add("1231");
        urls.add("1231");
        urls.add("1231");
        urls.add("1231");
        urls.add("1231");

        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.add(testBitmap);
        bitmaps.add(testBitmap);
        bitmaps.add(testBitmap);
        bitmaps.add(testBitmap);
        bitmaps.add(testBitmap);

        ArrayList<PotentialMatch> al = new ArrayList<PotentialMatch>();
        al.add(new PotentialMatch("idan 0","31","4",urls,bitmaps,"liked"));
        al.add(new PotentialMatch("idan 1 ","31","4",urls,bitmaps,"liked"));
        al.add(new PotentialMatch("idan 2 ","31","4",urls,bitmaps,"liked"));
        al.add(new PotentialMatch("idan 3","31","4",urls,bitmaps,"liked"));
        mPotentialMatches = al;
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
