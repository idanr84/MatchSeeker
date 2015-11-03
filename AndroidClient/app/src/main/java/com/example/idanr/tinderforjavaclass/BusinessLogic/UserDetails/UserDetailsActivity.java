package com.example.idanr.tinderforjavaclass.BusinessLogic.UserDetails;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;


import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.idanr.tinderforjavaclass.Model.BaseUser;
import com.example.idanr.tinderforjavaclass.Model.PotentialMatch;
import com.example.idanr.tinderforjavaclass.NetworkManager.NetworkManager;
import com.example.idanr.tinderforjavaclass.R;
import com.example.idanr.tinderforjavaclass.StorageManager.StorageManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by idanr on 10/26/15.
 */
public class UserDetailsActivity extends Activity{

    static final int NUM_ITEMS = 10;

    @Bind(R.id.userImages)
    ViewPager mUserImages;

    MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_details_activity);
        ButterKnife.bind(this);

        PotentialMatch potentialMatch = StorageManager.sharedInstance().getPotentialMatchAtIndex(0);

        mAdapter = new MyAdapter(getFragmentManager(),potentialMatch);
        mUserImages.setAdapter(mAdapter);

        TransitionSet animationSet = new TransitionSet();
        animationSet.addTransition(new ChangeBounds());
        animationSet.setDuration(500);
        getWindow().setSharedElementEnterTransition(animationSet);
        getWindow().setSharedElementExitTransition(animationSet);

        Slide slideEnterTransition = new Slide();
        slideEnterTransition.excludeChildren(R.id.userImages, true);
        slideEnterTransition.excludeChildren(R.id.container, true);
        slideEnterTransition.excludeTarget(android.R.id.navigationBarBackground, true);
        slideEnterTransition.excludeTarget(android.R.id.statusBarBackground, true);
        OvershootInterpolator overshoot = new OvershootInterpolator((float)0.5);
        slideEnterTransition.setInterpolator(overshoot);
        slideEnterTransition.setDuration(800);

        //slideEnterTransition.setStartDelay(200);
        getWindow().setEnterTransition(slideEnterTransition);

        Slide slideTransition = new Slide();
        slideTransition .excludeChildren(R.id.userImages, true);
        slideTransition .excludeChildren(R.id.container, true);
        slideTransition.excludeTarget(android.R.id.navigationBarBackground, true);
        slideTransition.excludeTarget(android.R.id.statusBarBackground, true);
        slideTransition .setDuration(800);
        getWindow().setReturnTransition(slideTransition);
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm, PotentialMatch potentialMatch){
            super(fm);
            mPtentialMatch = potentialMatch;
        }
        private PotentialMatch mPtentialMatch;

        @Override
        public int getCount() {
            return mPtentialMatch.getNumOfImages();
        }

        @Override
        public Fragment getItem(int position) {
            return UserImageFragment.newInstance(position,mPtentialMatch.getImageUrlAtIndex(position));
        }
    }

    public static class UserImageFragment extends Fragment {
        int mNum;
        String mImageUrl;

        /**
         * Create a new instance of UserImageFragment, providing "num"
         * as an argument.
         */
        static UserImageFragment newInstance(int num,String imageUrl) {
            UserImageFragment f = new UserImageFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            args.putString("imageUrl", imageUrl);
            f.setArguments(args);

            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
            mImageUrl = getArguments() != null ? (String)getArguments().getString("imageUrl") : null;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.user_images_pager, container, false);
            NetworkImageView userImage = (NetworkImageView)v.findViewById(R.id.image);
            userImage.setImageUrl(mImageUrl, NetworkManager.sharedInstance().getImageLoader());
            return v;
        }

    }
}
