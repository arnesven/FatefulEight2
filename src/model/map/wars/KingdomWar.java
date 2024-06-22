package model.map.wars;

import model.map.CastleLocation;

import java.io.Serializable;

public class KingdomWar implements Serializable {
    private final String aggressor;
    private final String defender;
    private int stage = 0;

    public KingdomWar(String aggressor, String defender) {
        this.aggressor = aggressor;
        this.defender = defender;
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
}
