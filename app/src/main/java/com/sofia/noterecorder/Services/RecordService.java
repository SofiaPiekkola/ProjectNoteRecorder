package com.sofia.noterecorder.Services;

import com.sofia.noterecorder.Activities.Settings;
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

public class RecordService extends Service {
    private String recordNote;
    private MediaRecorder recorder;

    private long startHTime = 0L;
    private final Handler customHandler = new Handler();
    private long timeSwapBuff = 0L;
    private final Intent broadcastIntent = new Intent("com.sofia.timeBroadcast");

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        recordNote = intent.getExtras().getString("note");
        boolean start = intent.getExtras().getBoolean("start");

        onRecord(start);
        return START_NOT_STICKY;
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        if (Settings.recordType == 1) recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
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

    private String createFile(){
        @SuppressWarnings("ConstantConditions")
        File folder = new File(getExternalCacheDir().getAbsolutePath() + "/" + recordNote + "/open/");
        String dateInString = new SimpleDateFormat("yyyy.MM.dd.", Locale.ENGLISH).format(new Date());
        File f;
        File f2;
        int i = 0;
        String length = "%03d";
        String type;
        if (Settings.recordType == 1) type = ".3gp";
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

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}