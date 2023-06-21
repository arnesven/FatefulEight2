package model.journal;

import java.awt.*;
import java.io.Serializable;

public class MainStorySpawnLocation implements Serializable {
    private String town;
    private String castle;
    private Point witch;

    public MainStorySpawnLocation(String town, String castle, Point witchLocation) {
        this.town = town;
        this.castle = castle;
        this.witch = witchLocation;
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
}
