package com.sofia.noterecorder.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sofia.noterecorder.Activitys.ListenRecordsActivity;
import com.sofia.noterecorder.R;
import com.sofia.noterecorder.Resources.SoundFile;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Sofia on 17.4.2017.
 */

public class ListFragment extends Fragment {
    ArrayList<SoundFile> notes = new ArrayList<>();
    ArrayList<SoundFile> sounds = new ArrayList<>();
    public View row;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSounds("/notes", notes);
        createSounds("/sounds", sounds);
    }

    private void createSounds(String file, ArrayList<SoundFile> list) {
        String path = getActivity().getExternalCacheDir().getAbsolutePath() + file;
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) list.add(new SoundFile(f.getName(), path));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list);
        ArrayAdapter<SoundFile> soundAdapter = new ArrayAdapter<>(getActivity(), R.layout.sound_file, notes);
        listView.setAdapter(soundAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (row != null) row.setBackgroundResource(R.color.colorPrimaryDark);
                row = view;
                view.setBackgroundResource(R.color.colorPrimary);
                ((ListenRecordsActivity)getActivity()).soundSelected(notes.get(position));
            }
        });
        return view;
    }
}
