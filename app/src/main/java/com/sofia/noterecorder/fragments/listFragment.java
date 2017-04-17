package com.sofia.noterecorder.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sofia.noterecorder.R;
import com.sofia.noterecorder.Resources.SoundFile;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Sofia on 17.4.2017.
 */

public class listFragment extends Fragment {
    ArrayList<SoundFile> notes = new ArrayList<>();
    ArrayList<SoundFile> sounds = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSounds("/notes", notes);
        createSounds("/sounds", sounds);
    }

    private void createSounds(String file, ArrayList list) {
        String path = getActivity().getExternalCacheDir().getAbsolutePath() + file;
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) System.out.println(f.getName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }
}
