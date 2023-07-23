package model.journal;

import java.awt.*;
import java.io.Serializable;

public class MainStorySpawnLocation implements Serializable {
    private final int mapExpand;
    private final Point camp;
    private String town;
    private String castle;
    private Point witch;
    private String libraryTown;

    public MainStorySpawnLocation(String town, String castle, Point witchLocation, String libraryTown, int expandedMapState, Point camp) {
        this.town = town;
        this.castle = castle;
        this.witch = witchLocation;
        this.libraryTown = libraryTown;
        this.mapExpand = expandedMapState;
        this.camp = camp;
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

    public int getExpandDirection() {
        return mapExpand;
    }

    public Point getCamp() {
        return camp;
    }
}
