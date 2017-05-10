package com.sofia.noterecorder.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.sofia.noterecorder.R;

@SuppressWarnings("UnusedParameters")
public class BaseActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }


    public void sendFeedBack(MenuItem item) {
        Intent Email = new Intent(Intent.ACTION_SEND);
        Email.setType("text/email");
        Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "noterecorder@outlook.com" });
        Email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback));
        Email.putExtra(Intent.EXTRA_TEXT, getString(R.string.add));
        startActivity(Intent.createChooser(Email, getString(R.string.selectMail)));
    }

    public void settings(MenuItem item) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void help(MenuItem item) {
        Intent intent = new Intent(this, Help.class);
        startActivity(intent);
    }
}