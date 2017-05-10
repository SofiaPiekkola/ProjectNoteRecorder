package com.sofia.noterecorder.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import com.sofia.noterecorder.R;
import com.sofia.noterecorder.fragments.PlayFragment;
import java.util.Locale;

/**
 * @author Sofia Piekkola
 * @version 1.0
 * @since 10.5.2017
 *
 *
 * PlayActivity sets up the play view for portrait orientation.
 */
public class PlayActivity extends BaseActivity {

    /**
     * Displays the time that is left when listening the recording
     */
    private TextView timeLeft;

    /**
     * Displays the time that is left when listening the recording
     */
    private SeekBar seekBar;

    /**
     * Full length of the seekBar
     */
    private int maxValue;

    /**
     * Used to capture time that is left when listening the recording
     */
    private MyBroadCastReceiver receiver;

    /**
     * Creates the view for portrait orientation
     *
     * @param savedInstanceState - mapping from String keys
     */
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
            if (name != null)
                textView.setText(name.substring(0, name.length() - 4));
            maxValue = (int) getIntent().getExtras().get("duration");
            setSeekBar();
        }
    }

    /**
     * Unregisters the receiver
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
        }catch (IllegalArgumentException e){
            System.out.println("Already unregistered");
        }
    }

    /**
     * Creates the play file fragment to display opened sound file.
     */
    @Override
    protected void onResume() {
        super.onResume();
        String name = (String) getIntent().getExtras().get("name");
        String path = (String) getIntent().getExtras().get("path");
        int duration = getIntent().getIntExtra("duration", 0);
        PlayFragment playFragment = (PlayFragment) getFragmentManager().findFragmentById(R.id.playFileFragment);
        playFragment.initialiseButtons(path, name, duration);
    }

    /**
     * Captures time that is left when listening the recording
     */
    public class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long mills = intent.getLongExtra("duration", 0);
            int secs = ((int) mills)/1000;
            int mins = secs / 60;
            String time = String.format(Locale.ENGLISH, "%02d", mins) + ":" + String.format(Locale.ENGLISH, "%02d", (secs % 60));
            timeLeft.setText(time);
            seekBar.setProgress(maxValue - secs);
        }
    }

    /**
     * Sets up the seekBar
     */
    private void setSeekBar() {
        seekBar.setProgress(0);
        seekBar.setMax(maxValue);
        seekBar.setOnTouchListener((view, motionEvent) -> true);
        int mins = maxValue / 60;
        int secs = maxValue % 60;
        String time = String.format(Locale.ENGLISH, "%02d", mins) + ":" + String.format(Locale.ENGLISH, "%02d", secs);
        timeLeft.setText(time);
    }
}