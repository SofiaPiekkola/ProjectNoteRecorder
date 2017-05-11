package com.sofia.noterecorder.fragments;

import android.support.v4.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sofia.noterecorder.Activities.ListenRecordsActivity;
import com.sofia.noterecorder.R;
import com.sofia.noterecorder.Resources.SoundFile;

import java.io.File;
import java.util.ArrayList;

public class ListFragment_sounds extends Fragment {
    private final ArrayList<SoundFile> sounds = new ArrayList<>();
    private View row;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSounds("/sounds/open", sounds);
        createSounds("/sounds/close", sounds);
    }

    private void createSounds(String file, ArrayList<SoundFile> list) {
        @SuppressWarnings("ConstantConditions")
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
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list);
        ArrayAdapter<SoundFile> soundAdapter = new ArrayAdapter<>(getActivity(), R.layout.sound_file,  R.id.text, sounds);
        listView.setAdapter(soundAdapter);
        addClickListener(listView);
        selectFirstIfInLandscape(listView);
        return view;
    }

    private void selectFirstIfInLandscape(ListView listView) {
        if (isAdded()) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                new Handler().post(() -> listView.performItemClick(listView.getChildAt(0), 0,
                        listView.getAdapter().getItemId(0)));
            }
        }
    }

    private void addClickListener(ListView listView) {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (view != null) {
                if (row != null) row.setBackgroundResource(R.color.colorPrimaryDark);
                row = view;
                view.setBackgroundResource(R.color.colorPrimary);
                ((ListenRecordsActivity) getActivity()).soundSelected(sounds.get(position));
            }
            else selectFirstIfInLandscape(listView);
        });
    }
}
