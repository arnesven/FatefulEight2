package model.states.battle;

import model.map.HexLocation;
import model.map.HillsLocation;

public class HillsBattleTerrain extends BattleTerrain {
    private static final HexLocation loc = new HillsLocation();

    public HillsBattleTerrain() {
        super("Hills", loc);
    }
}
