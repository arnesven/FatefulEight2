package model.combat;

import model.Model;
import model.states.CombatEvent;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public abstract class Combatant implements Serializable {
    private int currentHp;

    public abstract int getMaxHP();

    public void addToHP(int i) {
        currentHp = Math.max(0, Math.min(currentHp + i, getMaxHP()));
    }

    public int getHP() {
        return currentHp;
    }

    public abstract int getSpeed();

    protected void setCurrentHp(int hp) {
        currentHp = hp;
    }

    public abstract String getName();

    public abstract void drawYourself(ScreenHandler screenHandler, int xpos, int ypos, Sprite initiativeSymbol);

    public abstract Sprite getCombatCursor(Model model);

    public Point getCursorShift() {
        return new Point(0, 0);
    }

    public abstract int getWidth();
}
