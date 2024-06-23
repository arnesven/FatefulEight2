package model.states.battle;

import view.ScreenHandler;
import view.sprites.Sprite;

import java.awt.*;

public abstract class BattleUnit {

    private static final int DIRECTION_UP = 3;
    private static final int DIRECTION_LEFT = 2;
    private static final int DIRECTION_DOWN = 1;
    private static final int DIRECTION_RIGHT = 0;
    private final String name;
    private final int count;
    private int direction = DIRECTION_RIGHT;

    public BattleUnit(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public void drawYourself(ScreenHandler screenHandler, Point p) {
        Sprite spr = getSprites()[direction];
        screenHandler.register(spr.getName(), p, spr);
    }

    protected abstract Sprite[] getSprites();

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}
