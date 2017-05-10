package com.sofia.noterecorder.Services;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sofia.noterecorder.fragments.ListFragment;
import com.sofia.noterecorder.fragments.ListFragment_sounds;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ListFragment tab1 = new ListFragment();
                return tab1;
            case 1:
                ListFragment_sounds tab2 = new ListFragment_sounds();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
