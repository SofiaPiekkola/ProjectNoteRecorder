package com.sofia.noterecorder.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sofia.noterecorder.Activities.Records_Activity;
import com.sofia.noterecorder.R;
import com.sofia.noterecorder.Resources.SoundFile;

import java.io.File;
import java.util.ArrayList;

/**
 * List_sounds_Fragment displays the list of sounds used in the second tab of portrait orientation.
 *
 * @author Sofia Piekkola
 * @version 1.0
 * @since 10.5.2017
 */
public class List_sounds_Fragment extends Fragment {

    /**
     * List of all the notes in directory.
     */
    private final ArrayList<SoundFile> sounds = new ArrayList<>();

    /**
     * One row of the list
     */
    private View row;

    /**
     * Creates the list view
     *
     * @param savedInstanceState - mapping from String keys
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSounds("/sounds/open");
    }

    /**
     * Creates sounds to be displayed in the list
     *
     * @param path - end part of the path that contains sounds
     */
    @SuppressWarnings("ConstantConditions")
    private void createSounds(String path) {
        path = getActivity().getExternalCacheDir().getAbsolutePath() + path;
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) sounds.add(new SoundFile(f.getName(), path));
        }
    }

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
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list);
        ArrayAdapter<SoundFile> soundAdapter = new ArrayAdapter<>(getActivity(), R.layout.sound_file,  R.id.text, sounds);
        listView.setAdapter(soundAdapter);
        addClickListener(listView);
        return view;
    }

    /**
     * Add click listener for all the sounds in the list
     *
     * @param listView - list that contains sound items
     */
    private void addClickListener(ListView listView) {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (view != null) {
                if (row != null) row.setBackgroundResource(R.color.colorPrimaryDark);
                row = view;
                view.setBackgroundResource(R.color.colorPrimary);
                ((Records_Activity) getActivity()).soundSelected(sounds.get(position));
            }
        });
    }
}
