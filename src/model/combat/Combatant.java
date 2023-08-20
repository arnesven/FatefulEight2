package model.combat;

import model.Model;
import model.characters.GameCharacter;
import model.states.CombatEvent;
import model.states.DailyEventState;
import model.states.GameState;
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
    private List<Condition> conditions = new ArrayList<>();
    private boolean fortified = false;

    public abstract int getMaxHP();

    public void addToHP(int i) {
        int before = currentHp;
        currentHp = Math.max(0, Math.min(currentHp + i, getMaxHP()));
        if (currentHp > before) {
            removeCondition(BleedingCondition.class);
        }
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

    public void setFortified(boolean b) {
        fortified = b;
    }

    public boolean isFortified() {
        return fortified;
    }

    public void addCondition(Condition cond) {
        if (!hasCondition(cond.getClass())) {
            conditions.add(cond);
        } else {
            // TODO: Check if cond is a timed condition, add the time to the existing condition
        }
    }

    public boolean hasCondition(Class<? extends Condition> condition) {
        return getCondition(condition) != null;
    }

    public Condition getCondition(Class<? extends Condition> condition) {
        for (Condition cond : conditions) {
            if (cond.getClass().equals(condition)) {
                return cond;
            }
        }
        return null;
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
            found.wasRemoved(this);
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
            screenHandler.register(spr.getName(), new Point(xpos+count, ypos), spr);
            count++;
        }
    }

    public void removeCombatConditions() {
        List<Condition> toBeRemoved = new ArrayList<>();
        for (Condition cond : conditions) {
            if (cond.removeAtEndOfCombat()) {
                toBeRemoved.add(cond);
            }
        }
        for (Condition cond : toBeRemoved) {
            removeCondition(cond.getClass());
        }
    }

    public void conditionsEndOfDayTrigger(Model model, GameState state) {
        List<Condition> conditionsToTraverse = new ArrayList<>(conditions);
        for (Condition cond : conditionsToTraverse) {
            cond.endOfDayTrigger(model, state, this);
        }
    }

    public void takeCombatDamage(CombatEvent combatEvent, int damage) {
        addToHP(-damage);
    }

    public void conditionsEndOfCombatRoundTrigger(Model model, GameState state) {
        List<Condition> conditionsToTraverse = new ArrayList<>(conditions);
        for (Condition cond : conditionsToTraverse) {
            cond.endOfCombatRoundTrigger(model, state, this);
        }
    }

    public boolean canBeAttackedBy(GameCharacter gameCharacter) {
        return !fortified || gameCharacter.getEquipment().getWeapon().isRangedAttack();
    }


    public int getAttackBonusesFromConditions() {
        int sum = 0;
        for (Condition cond : conditions) {
            sum += cond.getAttackBonus();
        }
        return sum;
    }
}
