package model.journal;

import java.awt.*;
import java.io.Serializable;

public class MainStorySpawnLocation implements Serializable {
    private String town;
    private String castle;
    private Point witch;
    private String libraryTown;

    public MainStorySpawnLocation(String town, String castle, Point witchLocation, String libraryTown) {
        this.town = town;
        this.castle = castle;
        this.witch = witchLocation;
        this.libraryTown = libraryTown;
    }

    public String getTown() {
        return town;
    }

    public String getCastle() {
        return castle;
    }

    public Point getWitch() {
        return witch;
    }

    public String getLibraryTown() { return libraryTown; }
}
