package model.states.battle;

import model.map.HexLocation;
import model.map.WoodsLocation;

public class WoodsBattleTerrain extends BattleTerrain {
    private static final HexLocation loc = new WoodsLocation(false);

    public WoodsBattleTerrain() {
        super("Woods", loc);
    }

    @Override
    public int getCoverDefenseBonus(BattleState battleState) {
        battleState.println("Defender has partial cover (+1 to defense).");
        return 1;
    }
}
