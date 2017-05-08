package com.sofia.noterecorder.Resources;

public class SoundFile {
    String name;
    String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SoundFile(String name, String path) {
        this.name = name;
        this.path = path + "/" + name;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        if (name.contains(".3gp")) return name.replace(".3gp", "");
        else return name.replace(".mp4", "");
    }
}
