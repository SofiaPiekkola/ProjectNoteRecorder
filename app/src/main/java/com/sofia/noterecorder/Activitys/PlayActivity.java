package com.sofia.noterecorder.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sofia.noterecorder.R;
import com.sofia.noterecorder.fragments.PlayFragment;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
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
