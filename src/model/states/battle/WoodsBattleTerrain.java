package model.states.battle;

import model.map.HexLocation;
import model.map.WoodsLocation;

public class WoodsBattleTerrain extends BattleTerrain {
    public static final int COVER_BONUS = 1;
    private static final HexLocation loc = new WoodsLocation(false);

    public WoodsBattleTerrain() {
        super("Woods", loc);
    }

    @Override
    public int getCoverDefenseBonus(BattleState battleState) {
        battleState.println("Defender has partial cover (+" + COVER_BONUS + " to defense).");
        return 1;
    }

    @Override
    public String getHelpNote() {
        return "Cover " + COVER_BONUS;
    }
}
