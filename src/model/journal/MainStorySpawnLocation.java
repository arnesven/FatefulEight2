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
    private MyColors[] code;
    private final Point xelbiPosition;

    public MainStorySpawnLocation(String town, String castle, Point witchLocation, String libraryTown, int expandedMapState, Point camp,
                                  Point xelbiPosition, String remotePeopleName, Point remotePeoplePosition) {
        this.town = town;
        this.castle = castle;
        this.witch = witchLocation;
        this.libraryTown = libraryTown;
        this.mapExpand = expandedMapState;
        this.camp = camp;
        this.xelbiPosition = xelbiPosition;
        this.remotePeopleName = remotePeopleName;
        this.remotePeoplePosition = remotePeoplePosition;
    }

    public abstract GainSupportOfRemotePeopleTask makeRemoteKingdomSupportTask(Model model);

    public abstract List<GainSupportOfNeighborKingdomTask> makeNeighborKingdomTasks(Model model);

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
