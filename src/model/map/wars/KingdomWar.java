package model.map.wars;

import model.map.CastleLocation;
import model.map.WorldBuilder;
import model.states.battle.*;
import util.MyPair;
import util.MyRandom;
import view.MyColors;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KingdomWar implements Serializable {
    private final String aggressor;
    private final String defender;
    private int stage = 0;
    private List<BattleUnit> aggressorUnits;
    private List<BattleUnit> defenderUnits;

    public KingdomWar(String aggressor, String defender, MyColors aggressorColor, MyColors defenderColor) {
        this.aggressor = aggressor;
        this.defender = defender;
        aggressorUnits = makeInitialSetOfTroops(aggressor, aggressorColor);
        defenderUnits = makeInitialSetOfTroops(defender, defenderColor);
    }

    private List<BattleUnit> makeInitialSetOfTroops(String kingdom, MyColors color) {
        List<BattleUnit> units = new ArrayList<>();
        String name = CastleLocation.placeNameShort(kingdom);
        aggressorUnits.add(new ArchersUnit(MyRandom.randInt(10, 16), name, color));
        aggressorUnits.add(new SwordsmanUnit(MyRandom.randInt(8, 12), name, color));
        aggressorUnits.add(new KnightsUnit(MyRandom.randInt(4, 7), name, color));
        aggressorUnits.add(new PikemenUnit(MyRandom.randInt(10, 16), name, color));
        aggressorUnits.add(new MilitiaUnit(MyRandom.randInt(14, 20), name, color));
        return units;
    }

    public String getAggressor() {
        return aggressor;
    }

    public String getDefender() {
        return defender;
    }

    public boolean isAggressor(CastleLocation kingdom) {
        return aggressor.equals(kingdom.getPlaceName());
    }

    public boolean isDefender(CastleLocation kingdom) {
        return defender.equals(kingdom.getPlaceName());
    }

    public Point getBattlePosition(CastleLocation castle) {
        Point p = new Point(WorldBuilder.CROSSROADS_INN_POSITION);
        p.x--;
        p.y--;
        return p;
    }

    public List<BattleUnit> getAggressorUnits() {
        return aggressorUnits;
    }

    public List<BattleUnit> getDefenderUnits() {
        return defenderUnits;
    }

    public List<MyPair<Point, BattleTerrain>> getTerrains() {
        return List.of(
                new MyPair<>(new Point(2, 2), new WaterBattleTerrain()),
                new MyPair<>(new Point(0, 6), new HillsBattleTerrain()),
                new MyPair<>(new Point(3, 3), new WoodsBattleTerrain()),
                new MyPair<>(new Point(4, 4), new HillsBattleTerrain()),
                new MyPair<>(new Point(5, 5), new DenseWoodsBattleTerrain()),
                new MyPair<>(new Point(6, 4), new SwampBattleTerrain())
        );
    }

    public MyColors getGroundColor() {
        return MyColors.GREEN;
    }

    public String getCurrentBattleName() {
        return "at the Crossroads";
    }

    public void advance(boolean forAggressor) {
        // TODO
    }
}
