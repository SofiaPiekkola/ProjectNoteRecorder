package com.sofia.noterecorder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String noteState;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
