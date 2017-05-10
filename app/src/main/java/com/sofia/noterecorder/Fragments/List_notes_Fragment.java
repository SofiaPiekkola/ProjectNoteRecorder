package com.sofia.noterecorder.Fragments;

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
import com.sofia.noterecorder.Activities.Records_Activity;
import com.sofia.noterecorder.R;
import com.sofia.noterecorder.Resources.SoundFile;
import java.io.File;
import java.util.ArrayList;

/**
 * @author Sofia Piekkola
 * @version 1.0
 * @since 10.5.2017
 *
 *
 * List_notes_Fragment displays the list of notes used in the first tab of portrait orientation
 * This fragment is used also on the landscape view to display both notes and sounds.
 */
public class List_notes_Fragment extends Fragment {

    /**
     * List of all the notes in directory.
     */
    private final ArrayList<SoundFile> notes = new ArrayList<>();

    /**
     * One row of the list
     */
    private View row;

    /**
     * True if the view is not set.
     */
    private boolean viewNull;

    /**
     * Creates the list view
     *
     * @param savedInstanceState - mapping from String keys
     */
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
        if (files != null) for (File f : files) notes.add(new SoundFile(f.getName(), path));
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
        ArrayAdapter<SoundFile> soundAdapter = new ArrayAdapter<>(getActivity(), R.layout.sound_file,  R.id.text, notes);
        listView.setAdapter(soundAdapter);
        addClickListener(listView);
        selectFirstIfInLandscape(listView);
        addOpenNoteMarkers(listView);
        return view;
    }

    /**
     * Selects first item from the list if in landscape mode
     *
     * @param listView - list that contains sound items
     */
    private void selectFirstIfInLandscape(ListView listView) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            new Handler().post(() -> listView.performItemClick(listView.getChildAt(0), 0,
                    listView.getAdapter().getItemId(0)));
        }
    }

    /**
     * Add click listener for all the sounds in the list
     *
     * @param listView - list that contains sound items
     */
    private void addClickListener(ListView listView) {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (view != null){
                if (row != null) row.setBackgroundResource(R.color.colorPrimaryDark);
                row = view;
                view.setBackgroundResource(R.color.colorPrimary);
                ((Records_Activity) getActivity()).soundSelected(notes.get(position));
            }
            else if (viewNull){
                viewNull = false;
                selectFirstIfInLandscape(listView);
            }
        });
    }

    /**
     * Adds markers for all the sounds in the list
     *
     * @param listView - list that contains sound items
     */
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
