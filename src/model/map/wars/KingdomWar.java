package model.map.wars;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.map.CastleLocation;
import model.map.WorldBuilder;
import model.states.battle.*;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KingdomWar implements Serializable {
    private final String aggressor;
    private final String defender;
    private final List<BattleUnit> aggressorUnits;
    private final List<BattleUnit> defenderUnits;
    private final List<PitchedBattleSite> aggressorSites;
    private final List<PitchedBattleSite> defenderSites;
    private PitchedBattleSite currentSite;
    private AdvancedAppearance generalAppearance;

    public KingdomWar(String aggressor, String defender, MyColors aggressorColor, MyColors defenderColor,
                      List<PitchedBattleSite> aggressorSites, PitchedBattleSite initialSite,
                      List<PitchedBattleSite> defenderSites) {
        this.aggressor = aggressor;
        this.defender = defender;
        this.aggressorSites = aggressorSites;
        Collections.reverse(aggressorSites);
        this.defenderSites = defenderSites;
        this.currentSite = initialSite;
        aggressorUnits = makeInitialSetOfTroops(aggressor, aggressorColor);
        defenderUnits = makeInitialSetOfTroops(defender, defenderColor);
    }

    private List<BattleUnit> makeInitialSetOfTroops(String kingdom, MyColors color) {
        List<BattleUnit> units = new ArrayList<>();
        String name = CastleLocation.placeNameShort(kingdom);
        units.add(new ArchersUnit(MyRandom.randInt(10, 16), name, color));
        units.add(new SwordsmanUnit(MyRandom.randInt(8, 12), name, color));
        units.add(new KnightsUnit(MyRandom.randInt(4, 7), name, color));
        units.add(new PikemenUnit(MyRandom.randInt(10, 16), name, color));
        units.add(new MilitiaUnit(MyRandom.randInt(14, 20), name, color));
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
        return currentSite.getPosition();
    }

    public List<BattleUnit> getAggressorUnits() {
        return aggressorUnits;
    }

    public List<BattleUnit> getDefenderUnits() {
        return defenderUnits;
    }

    public List<MyPair<Point, BattleTerrain>> getTerrains() {
        return currentSite.getTerrain();
    }

    public MyColors getGroundColor() {
        return currentSite.getGroundColor();
    }

    public String getCurrentBattleName() {
        return currentSite.getName();
    }

    /**
     * Advances the war to the next battle site.
     * @param forAggressor true if war advances away from agressor
     * @return true if war is over
     */
    public boolean advance(boolean forAggressor) {
        if (forAggressor) {
            if (defenderSites.isEmpty()) {
                return true;
            }
            currentSite = defenderSites.remove(0);
        } else {
            if (aggressorSites.isEmpty()) {
                return true;
            }
            currentSite = aggressorSites.remove(0);
        }
        return false;
    }

    public boolean isInitialBattle() {
        return defenderSites.size() == 2 && aggressorSites.size() == 2;
    }

    public CharacterAppearance getGeneralAppearance() {
        if (generalAppearance == null) {
            generalAppearance = PortraitSubView.makeRandomPortrait(Classes.PAL);
        }
        return generalAppearance;
    }
}
