package com.sofia.noterecorder.Activitys;

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

public class MainActivity extends BaseActivity {
    String noteState;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    TimeReceiver timeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeReceiver = new TimeReceiver();
        registerReceiver(timeReceiver, new IntentFilter("com.sofia.timeBroadcast"));
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
    }

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

    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            unregisterReceiver(timeReceiver);
        }catch (IllegalArgumentException e){
            System.out.println("Already unregistered");
        }
    }

    public void record(View view) {
        Button b = (Button) findViewById(R.id.btnRecDiff);
        if (!b.getText().toString().toLowerCase().contains("note")) noteState = "notes";
        else noteState = "sounds";

        if (view.isActivated()) view.setActivated(false);
        else view.setActivated(true);

        Intent intent = new Intent(this, RecordService.class);
        intent.putExtra("note", noteState);
        intent.putExtra("start", view.isActivated());
        startService(intent);
    }

    public void soundOrNote(View view) {
        Button pressed = (Button) view;
        if (pressed.getText().toString().toLowerCase().contains("note") || pressed.getText().toString().toLowerCase().contains("muistiin")) pressed.setText(R.string.rec_sound);
        else pressed.setText(R.string.rec_note);
    }

    public void listenRecords(View view) {
        Intent intent = new Intent(this, ListenRecordsActivity.class);
        startActivity(intent);
    }

    public class TimeReceiver extends BroadcastReceiver {
        Button b = (Button) findViewById(R.id.recBtn);
        @Override
        public void onReceive(Context context, Intent intent) {
            String time = intent.getStringExtra("time");
            b.setText(time);
        }
    }
}
