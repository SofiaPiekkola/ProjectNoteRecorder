package com.sofia.noterecorder.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sofia.noterecorder.R;

/**
 * @author Sofia Piekkola
 * @version 1.0
 * @since 10.5.2017
 *
 *
 * Record_Fragment displays the record button in the first view.
 */
public class Record_Fragment extends Fragment {

    /**
     * Instantiates the view
     *
     * @param inflater - used to inflate any views in the fragment
     * @param container - parent view that the fragment's UI is attached to
     * @param savedInstanceState - a previous saved state
     * @return - view for the fragment's UI
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_record, container, false);
    }
}
