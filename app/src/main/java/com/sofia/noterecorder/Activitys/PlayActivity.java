package com.sofia.noterecorder.Activitys;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import com.sofia.noterecorder.R;
import com.sofia.noterecorder.fragments.PlayFragment;

public class PlayActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        setContentView(R.layout.activity_play);
        String name = (String) getIntent().getExtras().get("name");
        TextView textView = (TextView) findViewById(R.id.soundName);
        if (textView != null) textView.setText(name.substring(0, name.length() - 4));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String name = (String) getIntent().getExtras().get("name");
        String path = (String) getIntent().getExtras().get("path");
        PlayFragment playFragment = (PlayFragment) getFragmentManager().findFragmentById(R.id.playFileFragment);
        playFragment.initialiseButtons(path, name);
    }
}
