package model.states.battle;

import model.map.SwampLocation;

public class SwampBattleTerrain extends BattleTerrain {
    public SwampBattleTerrain() {
        super("Swamp", new SwampLocation());
    }

    @Override
    public int getMoveCost() {
        return ROUGH_TERRAIN_MOVE_COST;
    }
}
