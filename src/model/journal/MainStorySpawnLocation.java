package model.journal;

import view.MyColors;

import java.awt.*;
import java.io.Serializable;

public class MainStorySpawnLocation implements Serializable {
    private final int mapExpand;
    private final Point camp;
    private String town;
    private String castle;
    private Point witch;
    private String libraryTown;
    private MyColors[] code;
    private Point xelbiPosition;

    public MainStorySpawnLocation(String town, String castle, Point witchLocation, String libraryTown, int expandedMapState, Point camp,
                                  Point xelbiPosition) {
        this.town = town;
        this.castle = castle;
        this.witch = witchLocation;
        this.libraryTown = libraryTown;
        this.mapExpand = expandedMapState;
        this.camp = camp;
        this.xelbiPosition = xelbiPosition;
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

    public MyColors[] getCode() {
        return code;
    }

    public void setAncientStrongholdCode(MyColors[] generateCode) {
        this.code = generateCode;
    }

    public Point getXelbi() {
        return xelbiPosition;
    }
}
