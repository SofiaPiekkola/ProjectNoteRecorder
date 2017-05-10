package com.sofia.noterecorder.Fragments;

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

/**
 * @author Sofia Piekkola
 * @version 1.0
 * @since 10.5.2017
 *
 *
 * Play_Fragment displays the play view used to display selected sound file.
 */
@SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
public class Play_Fragment extends Fragment {
    private MediaPlayer mPlayer;
    private CountDownTimer countDownTimer;
    private String path;

    /**
     * Instantiates the view
     *
     * @param inflater - used to inflate any views in the fragment
     * @param container - parent view that the fragment's UI is attached to
     * @param savedInstanceState - a previous saved state
     * @return - view for the fragment's UI
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    /**
     * Initialises all the buttons in play view
     *
     * @param path - path of the selected sound file
     * @param name - name of the selected sound file
     * @param duration - duration of the selected sound file
     */
    public void initialiseButtons(String path, String name, int duration) {
        this.path = path;
        playButton(duration);
        mailButton();
        renameField(name);
        deleteButton();
        openButton();
    }

    /**
     * Initialises open/close button
     * Button is visible only if selected file is a note.
     */
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

    /**
     * Changes the button according to open/close state
     *
     * @param isOpen - true if note is open
     * @param open - button used to change open/close state
     */
    private void changeBtn(boolean isOpen, Button open) {
        TextView t = (TextView) getActivity().findViewById(R.id.recOpen);
        open.setActivated(isOpen);
        if (isOpen) t.setText(getString(R.string.close));
        else t.setText(getString(R.string.open));
    }

    /**
     * Initialises open/close button
     * Open/close button sets notes open or close when user clicks.
     *
     * @param oldPath - path to old file
     * @param newPath - path to new file
     */
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

    /**
     * Initialises the delete button
     * Delete button deletes the sound file.
     * User has to select OK from alert dialog for file to be deleted
     */
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

    /**
     * Initialises the rename file button
     * Rename button renames the file and is called when user clicks the sound name.
     *
     * @param name - name of the file currently
     */
    private void renameField(String name) {
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

    /**
     * Renames the file
     * @param oldName - old name to be replaced
     * @param newName - new name for the file
     */
    private void renameFile(String oldName, String newName) {
        String tempPath = path.replace(oldName, "");
        if (oldName.contains(".3gp")) newName = newName+".3gp";
        else newName = newName + ".mp4";
        File from = new File(tempPath, oldName);
        File to = new File(tempPath, newName);
        from.renameTo(to);
    }

    /**
     * Initialises the mail button
     * Mail button sends file to email. File is automatically added as attachment
     */
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

    /**
     * Initialises the play button
     * Play button is used to listen the recordings
     *
     * @param duration length of the selected sound file
     */
    private void playButton(int duration){
        Button play = (Button) getView().findViewById(R.id.playStop);
        play.setOnClickListener((View v) -> startPlay(play, duration));
    }

    /**
     * Stops playing when not on foreground
     */
    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null){
            mPlayer.stop();
            countDownTimer.cancel();
            countDownTimer.onFinish();
        }
    }

    /**
     * Starts and stops playing the file
     *
     * @param play - button that is pressed
     * @param duration - duration of the sound file
     */
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

    /**
     * Starts the timer used to display how much is left of the playing sound.
     * @param duration length of the sound
     */
    private void startTimer(int duration) {
        Intent broadcastIntent = new Intent("com.sofia.musicBroadcast");
        countDownTimer = new CountDownTimer((duration * 1000), 1000){

            /**
             * Fires broadcast on regular interval (1 second)
             * @param millisUntilFinished - amount of time until finished
             */
            @Override
            public void onTick(long millisUntilFinished) {
                broadcastIntent.putExtra("duration", millisUntilFinished);
                getActivity().sendBroadcast(broadcastIntent);
            }

            /**
             * Fires when the time is up
             */
            @Override
            public void onFinish() {
                long dur = ((long) duration) *1000;
                broadcastIntent.putExtra("duration", dur);
                getActivity().sendBroadcast(broadcastIntent);
            }
        }.start();
    }
}
