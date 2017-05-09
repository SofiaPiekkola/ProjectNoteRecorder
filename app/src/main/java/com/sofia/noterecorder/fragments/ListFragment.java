package com.sofia.noterecorder.fragments;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sofia.noterecorder.Activitys.ListenRecordsActivity;
import com.sofia.noterecorder.R;
import com.sofia.noterecorder.Resources.SoundFile;

import java.io.File;
import java.util.ArrayList;

public class ListFragment extends Fragment {
    ArrayList<SoundFile> notes = new ArrayList<>();
    public View row;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSounds("/notes/open", notes);
        createSounds("/notes/close", notes);
    }

    private void createSounds(String file, ArrayList<SoundFile> list) {
        String path = getActivity().getExternalCacheDir().getAbsolutePath() + file;
        System.out.println(path);
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
        ArrayAdapter<SoundFile> soundAdapter = new ArrayAdapter<>(getActivity(), R.layout.sound_file,  R.id.text, notes);
        listView.setAdapter(soundAdapter);
        addClickListener(listView);
        selectFirstIfInLandscape(listView);
        addOpenNoteMarkers(listView);
        return view;
    }

    private void selectFirstIfInLandscape(ListView listView) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    listView.performItemClick(listView.getChildAt(0), 0,
                            listView.getAdapter().getItemId(0));
                }
            });
        }
    }

    private void addClickListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view != null) {
                    if (row != null) row.setBackgroundResource(R.color.colorPrimaryDark);
                    row = view;
                    view.setBackgroundResource(R.color.colorPrimary);
                    ((ListenRecordsActivity) getActivity()).soundSelected(notes.get(position));
                }
                else selectFirstIfInLandscape(listView);
            }
        });
    }

    private void addOpenNoteMarkers(ListView listView) {
        listView.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < listView.getLastVisiblePosition() + 1; i++) {
                    View v = listView.getChildAt(i -
                            listView.getFirstVisiblePosition() + listView.getHeaderViewsCount());
                    if (v != null) {
                        System.out.println(notes.get(i).getPath());
                        TextView b = (TextView) v.findViewById(R.id.open_btn);
                        if (notes.get(i).getPath().contains("open")) b.setVisibility(View.VISIBLE);
                        else b.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }
}
