package com.sofia.noterecorder.Resources;

/**
 * Created by Sofia on 18.4.2017.
 */

public class SoundFile {
    int id;
    String name;

    public SoundFile(int soundID, String name) {
        this.id = soundID;
        this.name = name;
    }

    public int getSoundID() { return id; }

    public void setSoundID(int soundID) { this.id = soundID; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
