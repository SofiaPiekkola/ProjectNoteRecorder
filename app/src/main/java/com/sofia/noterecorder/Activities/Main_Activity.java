package com.sofia.noterecorder.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.sofia.noterecorder.R;
import com.sofia.noterecorder.Services.RecordService;
import java.io.File;

/**
 * Main_Activity displays the first view of the app.
 *
 * @author Sofia Piekkola
 * @version 1.0
 * @since 10.5.2017
 */
public class Main_Activity extends Base_Activity {

    /**
     * Permission to use microphone.
     */
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    /**
     * True if the permission is accepted.
     */
    private boolean permissionToRecordAccepted = false;

    /**
     * Array of permissions
     */
    private final String [] permissions = {Manifest.permission.RECORD_AUDIO};

    /**
     * BroadcastReceiver used to display the duration of the recording
     */
    private TimeReceiver timeReceiver;

    /**
     * Creates the first view and requests permissions.
     *
     * @param savedInstanceState - mapping from String keys
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeReceiver = new TimeReceiver();
        registerReceiver(timeReceiver, new IntentFilter("com.sofia.timeBroadcast"));
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        createFolder("/sounds/open/");
        createFolder("/notes/open/");
        createFolder("/sounds/close/");
        createFolder("/notes/close/");
    }

    /**
     * Callback for the result from requesting permissions.
     *
     * @param requestCode - request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions - requested permissions. Never null.
     * @param grantResults - grant results for the corresponding permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Unregisters the receiver.
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            unregisterReceiver(timeReceiver);
        }catch (IllegalArgumentException e){
            System.out.println("Already unregistered");
        }
    }

    /**
     * Starts recording.
     * Called when user clicks record-button and starts the Record service
     * that handles actual recording.
     *
     * @param view - pressed button. If activated, displays the microphone image.
     */
    public void record(View view) {
        Button b = (Button) findViewById(R.id.btnRecDiff);
        String noteState;
        if (b.getText().toString().toLowerCase().contains("note") ||
                b.getText().toString().toLowerCase().contains("muistiin")) noteState = "sounds";
        else noteState = "notes";

        if (view.isActivated()) view.setActivated(false);
        else view.setActivated(true);

        Intent intent = new Intent(this, RecordService.class);
        intent.putExtra("note", noteState);
        intent.putExtra("start", view.isActivated());
        startService(intent);
    }

    /**
     * Determines weather file to be recorded will be sound or note.
     *
     * @param view - pressed button
     */
    public void soundOrNote(View view) {
        Button pressed = (Button) view;
        if (pressed.getText().toString().toLowerCase().contains("note") ||
                pressed.getText().toString().toLowerCase().contains("muistiin")) {
            pressed.setText(R.string.rec_sound);
            Toast.makeText(this, getString(R.string.setToSound), Toast.LENGTH_SHORT).show();
        }
        else {
            pressed.setText(R.string.rec_note);
            Toast.makeText(this, getString(R.string.setToNote), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Opens new view for listening and editing files.
     *
     * @param view - pressed button. Not used.
     */
    @SuppressWarnings("UnusedParameters")
    public void listenRecords(View view) {
        Intent intent = new Intent(this, Records_Activity.class);
        startActivity(intent);
    }

    /**
     * Displays the duration of the recording.
     */
    public class TimeReceiver extends BroadcastReceiver {
        final Button b = (Button) findViewById(R.id.recBtn);
        @Override
        public void onReceive(Context context, Intent intent) {
            String time = intent.getStringExtra("time");
            b.setText(time);
        }
    }

    /**
     * Creates all folders used in this application.
     */
    @SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
    private void createFolder(String path){
        File folder = new File(getExternalCacheDir().getAbsolutePath() + path);
        folder.mkdirs();
    }
}
