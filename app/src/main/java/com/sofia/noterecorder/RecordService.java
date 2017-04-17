package com.sofia.noterecorder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        recorder.release();
        recorder = null;

        printFiles();
    }


    public void printFiles(){
        System.out.println("Content: ");
        String path1 = getExternalCacheDir().getAbsolutePath() + "/notes";
        String path2 = getExternalCacheDir().getAbsolutePath() + "/sounds";

        File directory1 = new File(path1);
        File directory2 = new File(path2);

        File[] files1 = directory1.listFiles();
        File[] files2 = directory2.listFiles();

        if (files1 != null) {
            System.out.println("Path: " + path1);
            for (File aFiles1 : files1) System.out.println(aFiles1.getName());
        }

        if (files2 != null) {
            System.out.println("Path: " + path2);
            for (File aFiles2 : files2) System.out.println(aFiles2.getName());
        }
    }

    public String createFile(){
        String path = getExternalCacheDir().getAbsolutePath() + "/" + recordNote;
        String dateInString = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.ENGLISH).format(new Date());
        String fileName = "/" + dateInString + ".3gp";
        mFileName = path + fileName;
        return mFileName;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
