package com.sofia.noterecorder.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.sofia.noterecorder.R;
import com.sofia.noterecorder.Resources.SoundFile;
import com.sofia.noterecorder.Services.PagerAdapter;
import com.sofia.noterecorder.fragments.PlayFragment;

public class ListenRecordsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_records);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.notes)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.sounds)));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        }
    }

    public void soundSelected(SoundFile soundFile) {
        PlayFragment playFragment = (PlayFragment) getFragmentManager().findFragmentById(R.id.playFileFragment);
        int duration = getFileDuration(soundFile.getPath());
        if ( playFragment!= null && playFragment.isVisible()){
            playFragment.initialiseButtons(soundFile.getPath(), soundFile.getName(), duration);
            //noinspection ConstantConditions
            playFragment.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }else {
            Intent intent = new Intent(this, PlayActivity.class);
            intent.putExtra("name", soundFile.getName());
            intent.putExtra("path", soundFile.getPath());
            intent.putExtra("duration", duration);
            startActivity(intent);
        }
    }

    private int getFileDuration(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int seconds = (Integer.parseInt(duration) / 1000);
        mmr.release();
        return seconds;
    }
}
