package model.states.battle;

import model.map.DeepWoodsLocation;
import model.map.HexLocation;

public class DenseWoodsBattleTerrain extends BattleTerrain {
    private static final HexLocation loc = new DeepWoodsLocation();
    public static final int COVER_BONUS = 3;

    public DenseWoodsBattleTerrain() {
        super("Dense Woods", loc);
    }

    @Override
    public int getMoveCost() {
        return BattleTerrain.ROUGH_TERRAIN_MOVE_COST;
    }

    @Override
    public int getCoverDefenseBonus(BattleState battleState) {
        battleState.println("Target has cover (+" + COVER_BONUS + " to defense).");
        return 3;
    }

    @Override
    public String getHelpNote() {
        return "Cover " + COVER_BONUS;
    }
}
