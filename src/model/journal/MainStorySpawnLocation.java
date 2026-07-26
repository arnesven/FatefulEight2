package model.journal;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.mainstory.GainSupportOfNeighborKingdomTask;
import model.mainstory.GainSupportOfRemotePeopleTask;
import model.mainstory.MainStoryPastData;
import model.map.WastelandHex;
import model.map.World;
import model.map.WorldBuilder;
import model.map.WorldHex;
import model.map.locations.MiningTownLocation;
import view.MyColors;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
    private final MainStoryPastData pastData;

    public MainStorySpawnLocation(String town, String castle, Point witchLocation, String libraryTown, int expandedMapState, Point camp,
                                  Point xelbiPosition, String remotePeopleName, Point remotePeoplePosition, MainStoryPastData pastData) {
        this.town = town;
        this.castle = castle;
        this.witch = witchLocation;
        this.libraryTown = libraryTown;
        this.mapExpand = expandedMapState;
        this.camp = camp;
        this.xelbiPosition = xelbiPosition;
        this.remotePeopleName = remotePeopleName;
        this.remotePeoplePosition = remotePeoplePosition;
        this.pastData = pastData;
    }

    public abstract GainSupportOfRemotePeopleTask makeRemotePeopleSupportTask(Model model);

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

    public World buildPastWorld() {
        Point dispairPoint = getDespairPoint();
        Point anguishPoint = getAnguishPoint();
        Point sorrowPoint = getSorrowPoint();
        Point desolationPoint = getDesolationPoint();

        World w = WorldBuilder.buildPastWorld(getPastUpperLeftCornerPoint(), Map.of(
                dispairPoint, new WastelandHex(0, 0, WorldBuilder.ORIGINAL),
                anguishPoint, new WastelandHex(0, 0, WorldBuilder.ORIGINAL),
                sorrowPoint, new WastelandHex(0, 0, WorldBuilder.ORIGINAL),
                desolationPoint, new WastelandHex(0, 0, WorldBuilder.ORIGINAL)));
        WorldHex hex = w.getHex(dispairPoint);
        hex.setLocation(new MiningTownLocation("Despair"));
        hex = w.getHex(anguishPoint);
        hex.setLocation(new MiningTownLocation("Anguish"));
        hex = w.getHex(sorrowPoint);
        hex.setLocation(new MiningTownLocation("Sorrow"));
        hex = w.getHex(desolationPoint);
        hex.setLocation(new MiningTownLocation("Desolation"));
        return w;
    }

    public Point getAnguishPoint() {
        return new Point(1, 13);
    }

    public Point getDespairPoint() {
        return new Point(1, 2);
    }

    public Point getSorrowPoint() {
        return new Point(17, 13);
    }

    public Point getDesolationPoint() {
        return new Point(18, 0);
    }

    public Point getPastEntryPoint() {
        return pastData.entryPoint;
    }

    public Point getPastUpperLeftCornerPoint() {
        return pastData.upperLeftCorner;
    }

    public abstract AdvancedAppearance getArabellaAppearance();

    public String getPastCapitalCity() {
        return pastData.capitalCity;
    }

    public String getPastCityA() {
        return pastData.cityA;
    }

    public String getPastCityB() {
        return pastData.cityB;
    }
}
