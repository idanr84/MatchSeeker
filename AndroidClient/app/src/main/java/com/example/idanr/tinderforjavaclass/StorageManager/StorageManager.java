package com.example.idanr.tinderforjavaclass.StorageManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.idanr.tinderforjavaclass.Model.BaseUser;
import com.example.idanr.tinderforjavaclass.R;

import java.util.ArrayList;

/**
 * Created by idanr on 10/28/15.
 */
public class StorageManager {

    static StorageManager mSharedInstace;
    private Context mContext;
    private ArrayList<BaseUser> mPotentialMatches;

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
        createTestPotentialMatches();
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

        ArrayList<BaseUser> al = new ArrayList<BaseUser>();
        al.add(new BaseUser("idan 0","31","4",urls,bitmaps));
        al.add(new BaseUser("idan 1 ","31","4",urls,bitmaps));
        al.add(new BaseUser("idan 2 ","31","4",urls,bitmaps));
        al.add(new BaseUser("idan 3","31","4",urls,bitmaps));
        mPotentialMatches = al;
    }

    public int getNumberOfPotentialMatches(){
        return mPotentialMatches.size();
    }

    public BaseUser getPotentialMatchAtIndex(int position){
        return mPotentialMatches.get(position);
    }

    public ArrayList<BaseUser> getPotentialMatches(){
        return mPotentialMatches;
    }


}
