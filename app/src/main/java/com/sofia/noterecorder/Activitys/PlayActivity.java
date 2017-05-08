package com.sofia.noterecorder.Activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sofia.noterecorder.R;
import com.sofia.noterecorder.fragments.PlayFragment;

public class PlayActivity extends BaseActivity {
    TextView timeLeft;
    SeekBar seekBar;
    int maxValue;
    MyBroadCastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
        } else {
            setContentView(R.layout.activity_play);
            seekBar = (SeekBar) findViewById(R.id.seekBar);
            timeLeft = (TextView) findViewById(R.id.timeLeft);
            receiver = new MyBroadCastReceiver();
            registerReceiver(receiver, new IntentFilter("com.sofia.musicBroadcast"));
            TextView textView = (TextView) findViewById(R.id.soundName);
            String name = (String) getIntent().getExtras().get("name");
            textView.setText(name.substring(0, name.length() - 4));
            maxValue = (int) getIntent().getExtras().get("duration");
            setSeekBar();
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
        }catch (IllegalArgumentException e){
            System.out.println("Already unregistered");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        String name = (String) getIntent().getExtras().get("name");
        String path = (String) getIntent().getExtras().get("path");
        int duration = getIntent().getIntExtra("duration", 0);
        PlayFragment playFragment = (PlayFragment) getFragmentManager().findFragmentById(R.id.playFileFragment);
        playFragment.initialiseButtons(path, name, duration);
    }

    private void setSeekBar() {
        seekBar.setProgress(0);
        seekBar.setMax(maxValue);
        seekBar.setOnTouchListener((view, motionEvent) -> true);
        int mins = maxValue / 60;
        int secs = maxValue - (60 * mins);
        timeLeft.setText(mins + ":" + secs);
    }

    private void updateTime(int duration) {
        int mins = duration / 60;
        int secs = duration - (60 * mins);
        timeLeft.setText(mins + ":" + secs);
        seekBar.setProgress(maxValue - duration);
    }

    public class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long mills = intent.getLongExtra("duration", 0);
            updateTime(((int)mills)/1000);
        }
    }
}