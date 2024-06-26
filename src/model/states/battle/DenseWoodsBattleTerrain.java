package model.states.battle;

import model.map.DeepWoodsLocation;
import model.map.HexLocation;

public class DenseWoodsBattleTerrain extends BattleTerrain {
    private static final HexLocation loc = new DeepWoodsLocation();

    public DenseWoodsBattleTerrain() {
        super("Dense Woods", loc);
    }

    @Override
    public int getMoveCost() {
        return BattleTerrain.ROUGH_TERRAIN_MOVE_COST;
    }
}
