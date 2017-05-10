package com.sofia.noterecorder.Services;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sofia.noterecorder.fragments.ListFragment_notes;
import com.sofia.noterecorder.fragments.ListFragment_sounds;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private final int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ListFragment_notes();
            case 1:
                return new ListFragment_sounds();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
