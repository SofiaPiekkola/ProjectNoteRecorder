package com.sofia.noterecorder.Services;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sofia.noterecorder.Fragments.List_notes_Fragment;
import com.sofia.noterecorder.Fragments.List_sounds_Fragment;

/**
 * PagerAdapter is used in creating tabs for the portrait view.
 *
 * @author Sofia Piekkola
 * @version 1.0
 * @since 10.5.2017
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    /**
     * Number of tabs on the screen
     */
    private final int mNumOfTabs;

    /**
     * Creates the page adapter
     *
     * @param fm - Interface for interacting with Fragment objects inside of an Activity
     * @param NumOfTabs - Number of tabs on the screen
     */
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    /**
     * Returns a view according to what tab is to be shown
     *
     * @param position - determines witch tab is to be shown
     * @return - a view to be shown
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new List_notes_Fragment();
            case 1:
                return new List_sounds_Fragment();
            default:
                return null;
        }
    }

    /**
     * Returns the number of tabs
     * @return - the number of tabs
     */
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
