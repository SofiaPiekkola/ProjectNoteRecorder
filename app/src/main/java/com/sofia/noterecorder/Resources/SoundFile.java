package com.sofia.noterecorder.Resources;

public class SoundFile {
    private final String name;
    private final String path;

    public String getPath() {
        return path;
    }

    public SoundFile(String name, String path) {
        this.name = name;
        this.path = path + "/" + name;
    }

    public String getName() { return name; }

    @Override
    public String toString() {
        if (name.contains(".3gp")) return name.replace(".3gp", "");
        else return name.replace(".mp4", "");
    }
}
