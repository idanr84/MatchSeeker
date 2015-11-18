package com.example.idanr.MatchSeeker.CustomUI.CardLayout.cardstack;

public class DefaultStackEventListener implements CardStack.CardEventListener {

    private float mThreshold;

    public DefaultStackEventListener(int i) {
        mThreshold = i;
    }

    @Override
    public boolean swipeEnd(int section, float distance) {
        if(distance > mThreshold){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean swipeStart(int section, float distance) {

        return false;
    }

    @Override
    public boolean swipeContinue(int section, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void discarded(int mIndex,int direction) {

    }

    @Override
    public void topCardTapped() {

    }


}
