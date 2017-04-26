package com.sofia.noterecorder.Activitys;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.sofia.noterecorder.R;
import com.sofia.noterecorder.Resources.SoundFile;
import com.sofia.noterecorder.fragments.PlayFragment;

public class ListenRecordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_records);
    }

    public void soundSelected(SoundFile soundFile) {
        PlayFragment playFragment = (PlayFragment) getFragmentManager().findFragmentById(R.id.playFileFragment);
        if ( playFragment!= null && playFragment.isVisible()){
            playFragment.initialiseButtons(soundFile.getPath(), soundFile.getName());
            playFragment.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }else {
            Intent intent = new Intent(this, PlayActivity.class);
            intent.putExtra("name", soundFile.getName());
            intent.putExtra("path", soundFile.getPath());
            startActivity(intent);
        }
    }
}
