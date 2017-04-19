package com.sofia.noterecorder.fragments;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sofia.noterecorder.R;

import java.io.IOException;

/**
 * Created by Sofia on 17.4.2017.
 */

public class PlayFragment extends Fragment {
    private MediaPlayer mPlayer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.play_fragment, container, false);
        return view;
    }

    public void initialiseButtons(String path, String name) {
        playButton(path);
        openButton();
        mailButton();
        renameButton();
        deleteButton();
    }

    private void deleteButton() {
        Button delete = (Button) getView().findViewById(R.id.delete);
        delete.setOnClickListener((View v) -> Toast.makeText(getActivity(), "DELETE FILE", Toast.LENGTH_SHORT).show());
    }

    private void renameButton() {
        Button rename = (Button) getView().findViewById(R.id.rename);
        rename.setOnClickListener((View v) -> Toast.makeText(getActivity(), "RENAME FILE", Toast.LENGTH_SHORT).show());
    }

    private void mailButton() {

    }

    private void openButton() {

    }

    public void playButton(String path){
        Button play = (Button) getView().findViewById(R.id.playStop);
        play.setOnClickListener((View v) -> startPlay(path, play));
    }

    private void startPlay(String path, Button play) {
        if (play.isActivated()){
            play.setActivated(false);
        }
        else {
            play.setActivated(true);
            mPlayer = new MediaPlayer();
            mPlayer.setOnCompletionListener(mp -> play.setActivated(false));
            try {
                mPlayer.setDataSource(path);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                System.out.println("Not working");
            }
        }
    }


//    public void openNote(View view) {
//        if (view.isActivated())view.setActivated(false);
//        else view.setActivated(true);
//        Toast.makeText(this, "OPEN NOTE (" + view.isActivated() + ")", Toast.LENGTH_SHORT).show();
//    }
//
//    public void sendMail(View view) {
//        Toast.makeText(this, "SEND EMAIL", Toast.LENGTH_SHORT).show();
//    }
//
//    public void renameFile(View view) {
//        Toast.makeText(this, "RENAME FILE", Toast.LENGTH_SHORT).show();
//    }
//
//    public void deleteRecord(View view) {
//        Toast.makeText(this, "DELETE FILE", Toast.LENGTH_SHORT).show();
//    }
}
