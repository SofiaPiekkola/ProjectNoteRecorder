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
import android.widget.TextView;

import com.sofia.noterecorder.Activities.ListenRecordsActivity;
import com.sofia.noterecorder.R;
import com.sofia.noterecorder.Resources.SoundFile;

import java.io.File;
import java.util.ArrayList;

public class ListFragment_notes extends Fragment {
    private final ArrayList<SoundFile> notes = new ArrayList<>();
    private View row;
    private boolean viewNull;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewNull = true;
        createSounds("/notes/open");
        createSounds("/notes/close");
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            createSounds("/sounds/open");
            createSounds("/sounds/close");
        }
    }

    private void createSounds(String file) {
        @SuppressWarnings("ConstantConditions")
        String path = getActivity().getExternalCacheDir().getAbsolutePath() + file;
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) notes.add(new SoundFile(f.getName(), path));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list);
        ArrayAdapter<SoundFile> soundAdapter = new ArrayAdapter<>(getActivity(), R.layout.sound_file,  R.id.text, notes);
        listView.setAdapter(soundAdapter);
        addClickListener(listView);
        selectFirstIfInLandscape(listView);
        addOpenNoteMarkers(listView);
        return view;
    }

    private void selectFirstIfInLandscape(ListView listView) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            new Handler().post(() -> listView.performItemClick(listView.getChildAt(0), 0,
                    listView.getAdapter().getItemId(0)));
        }
    }

    private void addClickListener(ListView listView) {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (view != null){
                if (row != null) row.setBackgroundResource(R.color.colorPrimaryDark);
                row = view;
                view.setBackgroundResource(R.color.colorPrimary);
                ((ListenRecordsActivity) getActivity()).soundSelected(notes.get(position));
            }
            else if (viewNull){
                viewNull = false;
                selectFirstIfInLandscape(listView);
            }
        });
    }

    private void addOpenNoteMarkers(ListView listView) {
        listView.post(() -> {
            for (int i = 0; i < listView.getLastVisiblePosition() + 1; i++) {
                View v = listView.getChildAt(i -
                        listView.getFirstVisiblePosition() + listView.getHeaderViewsCount());
                if (v != null) {
                    TextView b = (TextView) v.findViewById(R.id.open_btn);
                    if (notes.get(i).getPath().contains("notes/open")) b.setVisibility(View.VISIBLE);
                    else b.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
