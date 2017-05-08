package com.sofia.noterecorder.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;

import com.sofia.noterecorder.Activitys.Settings;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordService extends Service {
    String mFileName;
    String recordNote;
    private MediaRecorder recorder;

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
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(createFile());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            System.out.println("ERROR at startRecording (RecordService)");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;
    }

    public String createFile(){
        File folder = new File(getExternalCacheDir().getAbsolutePath() + "/" + recordNote + "/");
        folder.mkdirs();
        String dateInString = new SimpleDateFormat("yyyy.MM.dd.", Locale.ENGLISH).format(new Date());
        File f;
        File f2;
        int i = 0;
        String length = "%03d";
        String type;
        if (Settings.recordType == 1) type = ".3gp";
        else type = ".mp4";

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

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
