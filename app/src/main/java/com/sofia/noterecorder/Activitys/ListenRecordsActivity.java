package com.sofia.noterecorder.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sofia.noterecorder.R;

public class ListenRecordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_records);
    }

    public void openNote(View view) {
        if (view.isActivated())view.setActivated(false);
        else view.setActivated(true);
        Toast.makeText(this, "OPEN NOTE (" + view.isActivated() + ")", Toast.LENGTH_SHORT).show();
    }

    public void sendMail(View view) {
        Toast.makeText(this, "SEND EMAIL", Toast.LENGTH_SHORT).show();
    }

    public void playRecord(View view) {
        if (view.isActivated())view.setActivated(false);
        else view.setActivated(true);
        Toast.makeText(this, "PLAY FILE (" + view.isActivated() + ")", Toast.LENGTH_SHORT).show();
    }

    public void renameFile(View view) {
        Toast.makeText(this, "RENAME FILE", Toast.LENGTH_SHORT).show();
    }

    public void deleteRecord(View view) {
        Toast.makeText(this, "DELETE FILE", Toast.LENGTH_SHORT).show();
    }
}
