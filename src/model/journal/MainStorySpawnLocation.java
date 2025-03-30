package model.journal;

import model.Model;
import model.mainstory.GainSupportOfNeighborKingdomTask;
import model.mainstory.GainSupportOfRemotePeopleTask;
import view.MyColors;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public abstract class MainStorySpawnLocation implements Serializable {
    private final int mapExpand;
    private final Point camp;
    private final String remotePeopleName;
    private final Point remotePeoplePosition;
    private final String town;
    private final String castle;
    private final Point witch;
    private final String libraryTown;
    private final List<String> neighborCastles;
    private MyColors[] code;
    private final Point xelbiPosition;

    public MainStorySpawnLocation(String town, String castle, Point witchLocation, String libraryTown, int expandedMapState, Point camp,
                                  Point xelbiPosition, String remotePeopleName, Point remotePeoplePosition, List<String> neighborCastles) {
        this.town = town;
        this.castle = castle;
        this.witch = witchLocation;
        this.libraryTown = libraryTown;
        this.mapExpand = expandedMapState;
        this.camp = camp;
        this.xelbiPosition = xelbiPosition;
        this.remotePeopleName = remotePeopleName;
        this.remotePeoplePosition = remotePeoplePosition;
        this.neighborCastles = neighborCastles;
    }

    public abstract GainSupportOfRemotePeopleTask makeRemoteKingdomSupportTask(Model model);

    public List<GainSupportOfNeighborKingdomTask> makeNeighborKingdomTasks(Model model) {
        String castle1 = neighborCastles.get(0);
        String castle2 = neighborCastles.get(1);
        return List.of(new GainSupportOfNeighborKingdomTask(castle1,
                        model.getWorld().getPositionForLocation(model.getWorld().getLocationByName(castle1)), castle),
                       new GainSupportOfNeighborKingdomTask(castle2,
                        model.getWorld().getPositionForLocation(model.getWorld().getLocationByName(castle2)), castle));
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

    public String remotePeopleName() {
        return remotePeopleName;
    }

    public Point getRemotePeoplePosition() {
        return remotePeoplePosition;
    }
}
