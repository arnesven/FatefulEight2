package model.map.wars;

import model.map.CastleLocation;
import model.map.WorldBuilder;
import model.states.battle.BattleUnit;
import model.states.battle.SwordsmanUnit;
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
        aggressorUnits = new ArrayList<>(List.of(
                new SwordsmanUnit(10, CastleLocation.placeNameShort(aggressor), aggressorColor),
                new SwordsmanUnit(8, CastleLocation.placeNameShort(aggressor), aggressorColor)));
        defenderUnits = new ArrayList<>(List.of(new SwordsmanUnit(2, CastleLocation.placeNameShort(defender), defenderColor),
                new SwordsmanUnit(3, CastleLocation.placeNameShort(defender), defenderColor)));
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
