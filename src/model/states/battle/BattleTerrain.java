package model.states.battle;

import model.map.HexLocation;
import view.ScreenHandler;

import java.awt.*;

public abstract class BattleTerrain {

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
}
