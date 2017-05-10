package com.sofia.noterecorder.fragments;

import android.app.*;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
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
import android.widget.TextView;
import android.widget.Toast;
import com.sofia.noterecorder.R;
import java.io.File;
import java.io.IOException;

@SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
public class PlayFragment extends Fragment {
    private MediaPlayer mPlayer;
    private CountDownTimer countDownTimer;
    private String path;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    public void initialiseButtons(String path, String name, int duration) {
        this.path = path;
        playButton(duration);
        mailButton();
        renameButton(name);
        deleteButton();
        openButton();
    }

    private void openButton() {
        Button open = (Button) getView().findViewById(R.id.openClose);
        if (path.contains("open")) changeBtn(true, open);
        else changeBtn(false, open);
        if (path.contains("notes")) {
            open.setVisibility(View.VISIBLE);
            open.setClickable(true);
            getActivity().findViewById(R.id.recOpen).setVisibility(View.VISIBLE);
            open.setOnClickListener(v -> {
                if (path.contains("open")) {
                    setOpen("open", "close");
                    changeBtn(false, open);
                } else {
                    setOpen("close", "open");
                    changeBtn(true, open);
                }
            });
        }
        else {
            open.setVisibility(View.GONE);
            open.setClickable(false);
            TextView t = (TextView) getActivity().findViewById(R.id.recOpen);
            t.setVisibility(View.GONE);
        }
    }

    private void changeBtn(boolean b, Button open) {
        TextView t = (TextView) getActivity().findViewById(R.id.recOpen);
        open.setActivated(b);
        if (b) t.setText(getString(R.string.close));
        else t.setText(getString(R.string.open));
    }

    private void setOpen(String oldPath, String newPath) {
        String changed = path.replace(oldPath, newPath);
        File from = new File(path);
        File to = new File(changed);
        this.path = changed;
        from.renameTo(to);
        String toastText;
        if (newPath.equals("open")) toastText = getString(R.string.openToast);
        else toastText = getString(R.string.closeToast);

        Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show();
    }

    private void deleteButton() {
        Button delete = (Button) getView().findViewById(R.id.delete);
        delete.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AlertDialog alertDialog = new AlertDialog.Builder(this.getContext(), R.style.AlertDialogStyle)
                        .setMessage(R.string.confirm)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            File file = new File(path);
                            file.delete();
                            getActivity().onBackPressed();
                        })
                        .setNegativeButton(android.R.string.no, (dialog, which) -> {
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert).create();
                        alertDialog.setOnShowListener(dialog -> {
                            alertDialog.getButton(Dialog.BUTTON_POSITIVE).setTextSize(15);
                            alertDialog.getButton(Dialog.BUTTON_NEGATIVE).setTextSize(15);
                        });
                alertDialog.show();
                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                textView.setTextSize(20);
            }
        });
    }

    private void renameButton(String name) {
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
                            renameFile(name, String.valueOf(soundName.getText()));
                            getActivity().onBackPressed();
                        }
                        return handled;
                    });
                }
                return false;
            });
        }
    }

    private void renameFile(String oldName, String newName) {
        String tempPath = path.replace(oldName, "");
        if (oldName.contains(".3gp")) newName = newName+".3gp";
        else newName = newName + ".mp4";
        File from = new File(tempPath, oldName);
        File to = new File(tempPath, newName);
        from.renameTo(to);
    }

    private void mailButton() {
        Button mail = (Button) getView().findViewById(R.id.mail);
        mail.setOnClickListener(v -> {
            File fileIn = new File(path);
            Uri u = Uri.fromFile(fileIn);
            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_TEXT, "File attached");
            Email.putExtra(Intent.EXTRA_STREAM, u);
            startActivity(Intent.createChooser(Email, getString(R.string.selectMail)));
        });
    }

    private void playButton(int duration){
        Button play = (Button) getView().findViewById(R.id.playStop);
        play.setOnClickListener((View v) -> startPlay(play, duration));
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

    private void startPlay(Button play, int duration) {
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
