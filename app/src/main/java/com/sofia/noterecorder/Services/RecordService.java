package com.sofia.noterecorder.Services;

import com.sofia.noterecorder.Activities.Settings_Activity;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Sofia Piekkola
 * @version 1.0
 * @since 10.5.2017
 *
 *
 * RecordService is used to record the sound files.
 */
public class RecordService extends Service {

    /**
     * note or sound depending on users selections
     */
    private String recordNote;

    /**
     * Media recorder to record files
     */
    private MediaRecorder recorder;

    /**
     * Start time for counter
     */
    private long startHTime = 0L;

    /**
     * Handler for handling timer
     */
    private final Handler customHandler = new Handler();

    /**
     * Buffer for the timer
     */
    private long timeSwapBuff = 0L;

    /**
     * Broadcast used to show duration of the current recording
     */
    private final Intent broadcastIntent = new Intent("com.sofia.timeBroadcast");

    /**
     * Calls the onRecord to start or stop recording
     *
     * @param intent - The Intent supplied to startService(Intent), as given
     * @param flags -  Additional data about this start request
     * @param startId - A unique integer representing this specific request to start
     * @return - indicates what semantics the system should use for the service's current started state
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        recordNote = intent.getExtras().getString("note");
        boolean start = intent.getExtras().getBoolean("start");

        onRecord(start);
        return START_NOT_STICKY;
    }

    /**
     * Starts or stops recording
     *
     * @param start true if recording is to be started
     */
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    /**
     * Records a new sound file
     */
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        if (Settings_Activity.recordType == 1) recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        else recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(createFile());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            System.out.println("ERROR at startRecording (RecordService)");
        }

        recorder.start();
        startHTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    /**
     * Stops recording and restarts the recorder and timer
     */
    private void stopRecording() {
        recorder.stop();
        broadcastIntent.putExtra("time", "");
        sendBroadcast(broadcastIntent);
        customHandler.removeCallbacks(updateTimerThread);
        timeSwapBuff = 0L;
        recorder.reset();
        recorder.release();
        recorder = null;
    }

    /**
     * Creates a new sound file
     *
     * @return the complete path of the sound file
     */
    private String createFile(){
        @SuppressWarnings("ConstantConditions")
        File folder = new File(getExternalCacheDir().getAbsolutePath() + "/" + recordNote + "/open/");
        String dateInString = new SimpleDateFormat("yyyy.MM.dd.", Locale.ENGLISH).format(new Date());
        File f;
        File f2;
        int i = 0;
        String length = "%03d";
        String type;
        if (Settings_Activity.recordType == 1) type = ".3gp";
        else type = ".mp4";
        String mFileName;

        do{
            i++;
            if((i + "").length() > 2) length = "%0" + (i + "").length() + "d";
            String s = String.format(Locale.ENGLISH, length, i);
            String fileName = "/" + dateInString + s;
            mFileName = folder.getPath() + fileName;
            f = new File(mFileName + ".3gp");
            f2 = new File(mFileName + ".mp4");
        }while (f.exists() || f2.exists());

        return mFileName + type;
    }

    /**
     * Creates a new thread to show record timer
     */
    private final Runnable updateTimerThread = new Runnable() {
        public void run() {
            long timeInMilliseconds = SystemClock.uptimeMillis() - startHTime;
            long updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            String time = String.format(Locale.ENGLISH, "%02d", mins) + ":" + String.format(Locale.ENGLISH, "%02d", secs);
            broadcastIntent.putExtra("time", time);
            sendBroadcast(broadcastIntent);
            customHandler.postDelayed(this, 0);
        }
    };

    /**
     * Base interface for a remote object
     *
     * @param intent - Intent that was used to bind to this service
     * @return IBinder through which clients can call on to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented");
    }
}