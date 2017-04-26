package com.sofia.noterecorder.fragments;

import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sofia.noterecorder.R;

import java.io.File;
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
        return inflater.inflate(R.layout.play_fragment, container, false);
    }

    public void initialiseButtons(String path, String name) {
        playButton(path);
        openButton();
        mailButton();
        renameButton(path, name);
        deleteButton(path);
    }

    private void deleteButton(String path) {
        Button delete = (Button) getView().findViewById(R.id.delete);
        delete.setOnClickListener(v -> {
            File file = new File(path);
            file.delete();
            getActivity().onBackPressed();
        });
    }

    private void renameButton(String path, String name) {
        Button rename = (Button) getView().findViewById(R.id.rename);
        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) getActivity().findViewById(R.id.soundName);
                et.setEnabled(true);
                et.setSelectAllOnFocus(true);
                et.clearFocus();
                et.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
                et.setOnEditorActionListener((v1, actionId, event) -> {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        imm.hideSoftInputFromWindow(v1.getWindowToken(), 0);
                        et.setEnabled(false);
                        handled = true;
                        renameFile(path, name, String.valueOf(et.getText()));
                        getActivity().onBackPressed();
                    }
                    return handled;
                });

            }
        });
    }

    private void renameFile(String path, String oldName, String newName) {
        path = path.replace(oldName, "");
        newName = newName+".3gp";
        File from = new File(path, oldName);
        File to = new File(path, newName);
        from.renameTo(to);
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
            mPlayer.stop();
        }
        else {
            play.setActivated(true);
            mPlayer = new MediaPlayer();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    play.setActivated(false);
                }
            });
            try {
                mPlayer.setDataSource(path);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                System.out.println("Not working");
            }
        }
    }
}
