package com.example.idanr.tinderforjavaclass.BusinessLogic.UserDetails;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.example.idanr.tinderforjavaclass.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by idanr on 10/26/15.
 */
public class UserDetailsActivity extends Activity{

    @Bind(R.id.userImages)
    ImageView mUserImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_details_activity);
        ButterKnife.bind(this);



        TransitionSet animationSet = new TransitionSet();
        animationSet.addTransition(new ChangeBounds());
        animationSet.setDuration(500);
        getWindow().setSharedElementEnterTransition(animationSet);
        getWindow().setSharedElementExitTransition(animationSet);

//        Fade fadeTransmition= new Fade();
//        fadeTransmition.excludeChildren(R.id.userImages, true);
//        fadeTransmition.excludeChildren(R.id.container, true);
//        fadeTransmition.setDuration(5000);
//        getWindow().setEnterTransition(fadeTransmition);

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
}
