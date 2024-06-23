package model.states.battle;

import model.map.HexLocation;
import model.map.WoodsLocation;

public class WoodsBattleTerrain extends BattleTerrain {
    private static final HexLocation loc = new WoodsLocation(false);

    public WoodsBattleTerrain() {
        super("Woods", loc);
    }
}
