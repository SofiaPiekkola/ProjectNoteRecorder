package com.sofia.noterecorder.Activitys;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;

import com.sofia.noterecorder.R;
import com.sofia.noterecorder.Resources.SoundFile;
import com.sofia.noterecorder.fragments.PlayFragment;

public class ListenRecordsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_records);
    }

    public void soundSelected(SoundFile soundFile) {
        PlayFragment playFragment = (PlayFragment) getFragmentManager().findFragmentById(R.id.playFileFragment);
        int duration = getFileDuration(soundFile.getPath());
        if ( playFragment!= null && playFragment.isVisible()){
            playFragment.initialiseButtons(soundFile.getPath(), soundFile.getName(), duration);
            playFragment.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }else {
            Intent intent = new Intent(this, PlayActivity.class);
            intent.putExtra("name", soundFile.getName());
            intent.putExtra("path", soundFile.getPath());
            intent.putExtra("duration", duration);
            startActivity(intent);
        }
    }

    public int getFileDuration(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int seconds = (Integer.parseInt(duration) / 1000);
        mmr.release();
        return seconds;
    }
}
