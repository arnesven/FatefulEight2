package model.states.battle;

import model.map.HexLocation;
import view.ScreenHandler;

import java.awt.*;

public abstract class BattleTerrain {

    public static final int DEFAULT_MOVE_COST = 2;
    protected static final int ROUGH_TERRAIN_MOVE_COST = 3;
    protected static final int IMPASSIBLE_TERRAIN_MOVE_COST = Integer.MAX_VALUE;
    private final HexLocation loc;
    private final String name;

    public BattleTerrain(String name, HexLocation loc) {
        this.name = name;
        this.loc = loc;
    }

    public void drawYourself(ScreenHandler screenHandler, Point p) {
        loc.drawUpperHalf(screenHandler, p.x, p.y, 0);
        loc.drawLowerHalf(screenHandler, p.x, p.y);
    }

    public String getName() {
        return name;
    }

    public int checkForMeleeDefenseBonus(BattleState battleState, BattleUnit defender) {
        return 0;
    }

    public int getMoveCost() {
        return DEFAULT_MOVE_COST;
    }
}
