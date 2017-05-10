package com.sofia.noterecorder.Activities;

import android.os.Bundle;
import com.sofia.noterecorder.R;

/**
 * @author Sofia Piekkola
 * @version 1.0
 * @since 10.5.2017
 *
 *
 * Help_Activity displays the help.
 */
public class Help_Activity extends Base_Activity {

    /**
     * Creates help view
     *
     * @param savedInstanceState - mapping from String keys
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }
}
