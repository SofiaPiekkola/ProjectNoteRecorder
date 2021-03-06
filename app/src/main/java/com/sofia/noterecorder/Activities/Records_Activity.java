package com.sofia.noterecorder.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.sofia.noterecorder.Fragments.Play_Fragment;
import com.sofia.noterecorder.R;
import com.sofia.noterecorder.Resources.SoundFile;
import com.sofia.noterecorder.Services.PagerAdapter;

/**
 * Records_Activity displays the play view.
 *
 * @author Sofia Piekkola
 * @version 1.0
 * @since 10.5.2017
 */
public class Records_Activity extends Base_Activity {

    /**
     * Creates play view
     * If in portrait, creates the tabs to display notes and sounds on different tabs.
     *
     * @param savedInstanceState - mapping from String keys
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setTheme(R.style.AppTheme);
        else setTheme(R.style.DefaultTheme);
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
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }
    }

    /**
     * Opens play view for selected file
     * If in landscape, selects file. If in portrait, opens new view for sound file.
     *
     * @param soundFile - selected sound file
     */
    public void soundSelected(SoundFile soundFile) {
        Play_Fragment playFragment = (Play_Fragment) getFragmentManager().findFragmentById(R.id.playFileFragment);
        int duration = getFileDuration(soundFile.getPath());
        if ( playFragment!= null && playFragment.isVisible()){
            playFragment.initialiseButtons(soundFile.getPath(), soundFile.getName(), duration);
            //noinspection ConstantConditions
            playFragment.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }else {
            Intent intent = new Intent(this, Play_Activity.class);
            intent.putExtra("name", soundFile.getName());
            intent.putExtra("path", soundFile.getPath());
            intent.putExtra("duration", duration);
            startActivity(intent);
        }
    }

    /**
     * Returns duration of selected file
     *
     * @param path - path for selected file
     * @return - how many seconds does selected sound file last
     */
    private int getFileDuration(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int seconds = (Integer.parseInt(duration) / 1000);
        mmr.release();
        return seconds;
    }
}
