package model.combat;

import view.sprites.Sprite;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public abstract class Condition implements Serializable {
    private String name;
    private String shortName;
    private int duration = -1;

    public Condition(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    protected String getShortName() {
        return shortName;
    }

    public static boolean disablesCombatTurn(Set<Condition> conditions) {
        for (Condition c : conditions) {
            if (c.noCombatTurn()) {
                return true;
            }
        }
        return false;
    }

    public static String getCharacterStatus(Set<Condition> conditions) {
        StringBuilder bldr = new StringBuilder();
        for (Condition c : conditions) {
            bldr.append(c.getShortName() + ", ");
        }
        bldr.replace(bldr.length()-2, bldr.length(), "");
        return bldr.toString();
    }

    protected abstract boolean noCombatTurn();

    protected boolean hasDuration() {
        return duration != -1;
    }

    public void addToDuration(int i) {
        duration += i;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public abstract Sprite getSymbol();
}
