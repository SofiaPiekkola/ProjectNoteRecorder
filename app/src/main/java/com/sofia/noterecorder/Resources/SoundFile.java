package com.sofia.noterecorder.Resources;

/**
 * @author Sofia Piekkola
 * @version 1.0
 * @since 10.5.2017
 *
 *
 * SoundFile is the class for sound files used in this application
 */
public class SoundFile {
    private final String name;
    private final String path;

    /**
     * Gets the path of a sound file
     * @return - the path of a sound file
     */
    public String getPath() {
        return path;
    }

    /**
     * Creates a sound file
     *
     * @param name name of the file
     * @param path path for the file
     */
    public SoundFile(String name, String path) {
        this.name = name;
        this.path = path + "/" + name;
    }

    /**
     * Gets the name of the file
     *
     * @return - the name of the file
     */
    public String getName() { return name; }

    /**
     * Returns the string value of sound file
     *
     * @return - string value of sound file
     */
    @Override
    public String toString() {
        if (name.contains(".3gp")) return name.replace(".3gp", "");
        else return name.replace(".mp4", "");
    }
}
