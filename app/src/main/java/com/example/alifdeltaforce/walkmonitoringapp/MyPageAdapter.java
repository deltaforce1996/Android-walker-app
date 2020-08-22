package com.example.alifdeltaforce.walkmonitoringapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

/**
 * Created by Alif Delta Force on 6/24/2018.
 */

public class MyPageAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    private final int NUM_ITEMS = 2;
    private final String CURRENT_STEP_POSITION_KEY = "CURRENT_STEP_POSITION_KEY";

    public MyPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                final OneRegisterFragment step1 = new OneRegisterFragment();
                Bundle b1 = new Bundle();
                b1.putInt(CURRENT_STEP_POSITION_KEY, position);
                step1.setArguments(b1);
                return step1;
            case 1:
                final TwoRegisterFragment step2 = new TwoRegisterFragment();
                Bundle b2 = new Bundle();
                b2.putInt(CURRENT_STEP_POSITION_KEY, position);
                step2.setArguments(b2);
                return step2;
        }
        return null;
    }

    @Override
    public int getIconResId(int index) {
        return 0;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
