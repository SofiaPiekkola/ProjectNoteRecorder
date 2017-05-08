package com.sofia.noterecorder.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import com.sofia.noterecorder.R;

import java.io.File;
import java.io.IOException;

public class PlayFragment extends Fragment {
    private MediaPlayer mPlayer;
    private CountDownTimer countDownTimer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.play_fragment, container, false);
    }

    public void initialiseButtons(String path, String name, int duration) {
        playButton(path, duration);
        openButton();
        mailButton(path);
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
        EditText soundName = (EditText) getActivity().findViewById(R.id.soundName);
        if (soundName != null) {
            soundName.setCursorVisible(false);
            soundName.setOnTouchListener((v, eventMotion) -> {
                if(MotionEvent.ACTION_UP == eventMotion.getAction()){
                    soundName.setCursorVisible(true);
                    soundName.setSelectAllOnFocus(true);
                    soundName.clearFocus();
                    soundName.requestFocus();
                    soundName.setOnEditorActionListener((v1, actionId, event) -> {
                        boolean handled = false;
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            soundName.setEnabled(false);
                            handled = true;
                            renameFile(path, name, String.valueOf(soundName.getText()));
                            getActivity().onBackPressed();
                        }
                        return handled;
                    });
                }
                return false;
            });
        }
    }

    private void renameFile(String path, String oldName, String newName) {
        path = path.replace(oldName, "");
        newName = newName+".3gp";
        File from = new File(path, oldName);
        File to = new File(path, newName);
        from.renameTo(to);
    }

    private void mailButton(String path) {
        Button mail = (Button) getView().findViewById(R.id.mail);
        mail.setOnClickListener(v -> {
            File fileIn = new File(path);
            Uri u = Uri.fromFile(fileIn);
            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_TEXT, "File attached");
            Email.putExtra(Intent.EXTRA_STREAM, u);
            startActivity(Intent.createChooser(Email, "Select your e-mail service:"));
        });
    }

    private void openButton() {

    }

    public void playButton(String path, int duration){
        Button play = (Button) getView().findViewById(R.id.playStop);
        play.setOnClickListener((View v) -> startPlay(path, play, duration));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null){
            mPlayer.stop();
            countDownTimer.cancel();
            countDownTimer.onFinish();
        }
    }

    private void startPlay(String path, Button play, int duration) {
        if (play.isActivated()) {
            play.setActivated(false);
            mPlayer.stop();
            countDownTimer.cancel();
            countDownTimer.onFinish();
        } else {
            play.setActivated(true);
            mPlayer = new MediaPlayer();
            startTimer(duration);
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

    private void startTimer(int duration) {
        Intent broadcastIntent = new Intent("com.sofia.musicBroadcast");
        countDownTimer = new CountDownTimer((duration * 1000), 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                broadcastIntent.putExtra("duration", millisUntilFinished);
                getActivity().sendBroadcast(broadcastIntent);
            }

            @Override
            public void onFinish() {
                long dur = ((long) duration) *1000;
                broadcastIntent.putExtra("duration", dur);
                getActivity().sendBroadcast(broadcastIntent);
            }
        }.start();
    }
}
