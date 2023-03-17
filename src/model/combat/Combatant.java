package model.combat;

import model.Model;
import model.states.CombatEvent;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.CharSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Combatant implements Serializable {
    private int currentHp;
    private Set<Condition> conditions = new HashSet<>();

    public abstract int getMaxHP();

    public void addToHP(int i) {
        currentHp = Math.max(0, Math.min(currentHp + i, getMaxHP()));
    }

    public int getHP() {
        return currentHp;
    }

    public boolean isDead() { return getHP() <= 0; }

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

    public boolean getsCombatTurn() {
        return !isDead() && !Condition.disablesCombatTurn(conditions);
    }

    public void addCondition(Condition cond) {
        if (!hasCondition(cond.getClass())) {
            conditions.add(cond);
        } else {
            // TODO: Check if cond is a timed condition, add the time to the existing condition
        }
    }

    public boolean hasCondition(Class<? extends Condition> condition) {
        for (Condition cond : conditions) {
            if (cond.getClass().equals(condition)) {
                return true;
            }
        }
        return false;
    }

    public void removeCondition(Class<? extends Condition> condition) {
        Condition found = null;
        for (Condition cond : conditions) {
            if (cond.getClass().equals(condition)) {
                found = cond;
            }
        }
        if (found != null) {
            conditions.remove(found);
        }
    }

    public String getStatus() {
        if (isDead()) {
            return "DEAD";
        }
        if (conditions.isEmpty()) {
            return "OK";
        }
        return Condition.getCharacterStatus(conditions);
    }

    public void decreaseTimedConditions(Model model, CombatEvent event) {
        List<Condition> toRemove = new ArrayList<>();
        for (Condition cond : conditions) {
            if (cond.hasDuration()) {
                cond.addToDuration(-1);
            }
            if (cond.getDuration() == 0) {
                toRemove.add(cond);
            }
        }

        for (Condition cond : toRemove) {
            event.println(getName() + " no longer has condition " + cond.getName() + ".");
            removeCondition(cond.getClass());
        }
    }

    protected void drawConditions(ScreenHandler screenHandler, int xpos, int ypos) {
        int count = 0;
        for (Condition cond : conditions) {
            Sprite spr = cond.getSymbol();
            screenHandler.register(spr.getName(), new Point(xpos, ypos - count), spr);
        }
    }
}
