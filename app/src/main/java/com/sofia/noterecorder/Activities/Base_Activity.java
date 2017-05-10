package com.sofia.noterecorder.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.sofia.noterecorder.R;

/**
 * @author Sofia Piekkola
 * @version 1.0
 * @since 10.5.2017
 *
 *
 * Base_Activity displays the menu. This is the base class for all other activities.
 */
@SuppressWarnings("UnusedParameters")
public class Base_Activity extends AppCompatActivity {
    /**
     * Creates menu
     * @param menu - menu to be created
     * @return - true if successful
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    /**
     * Sends feedback via email
     * Called when user clicks send feedback from menu.
     *
     * @param item - pressed button, not used
     */
    public void sendFeedBack(MenuItem item) {
        Intent Email = new Intent(Intent.ACTION_SEND);
        Email.setType("text/email");
        Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "noterecorder@outlook.com" });
        Email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback));
        Email.putExtra(Intent.EXTRA_TEXT, getString(R.string.add));
        startActivity(Intent.createChooser(Email, getString(R.string.selectMail)));
    }

    /**
     * Opens settings view
     * Called when user clicks settings from menu.
     *
     * @param item - pressed button, not used
     */
    public void settings(MenuItem item) {
        Intent intent = new Intent(this, Settings_Activity.class);
        startActivity(intent);
    }

    /**
     * Opens help view
     * Called when user clicks help from menu.
     *
     * @param item - pressed button, not used
     */
    public void help(MenuItem item) {
        Intent intent = new Intent(this, Help_Activity.class);
        startActivity(intent);
    }
}
