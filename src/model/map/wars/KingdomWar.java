package model.map.wars;

import model.map.CastleLocation;
import model.map.WorldBuilder;
import model.states.battle.*;
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
        String aggressorName = CastleLocation.placeNameShort(aggressor);
        aggressorUnits = new ArrayList<>();
        for (int i = 2; i <= 12; i +=4) {
            aggressorUnits.add(new SwordsmanUnit(i, aggressorName, aggressorColor));
        }
        for (int i = 2; i <= 12; i +=4) {
            aggressorUnits.add(new PikemenUnit(i, aggressorName, aggressorColor));
        }
        aggressorUnits.add(new KnightsUnit(4, aggressorName, aggressorColor));
        aggressorUnits.add(new KnightsUnit(2, aggressorName, aggressorColor));
        aggressorUnits.add(new ArchersUnit(6, aggressorName, aggressorColor));

        String defenderName = CastleLocation.placeNameShort(defender);
        defenderUnits = new ArrayList<>();
        for (int i = 2; i <= 12; i +=4) {
            defenderUnits.add(new SwordsmanUnit(i, defenderName, defenderColor));
        }
        for (int i = 2; i <= 12; i +=4) {
            defenderUnits.add(new PikemenUnit(i, defenderName, defenderColor));
        }
        defenderUnits.add(new KnightsUnit(4, defenderName, defenderColor));
        defenderUnits.add(new KnightsUnit(2, defenderName, defenderColor));
        defenderUnits.add(new ArchersUnit(6, defenderName, defenderColor));
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
}
