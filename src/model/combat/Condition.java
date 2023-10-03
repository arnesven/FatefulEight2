package model.combat;

import model.Model;
import model.characters.GameCharacter;
import model.states.GameState;
import view.GameView;
import view.help.ConditionHelpDialog;
import view.help.HelpDialog;
import view.help.HelpView;
import view.sprites.Sprite;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
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

    public String getShortName() {
        return shortName;
    }

    public static boolean disablesCombatTurn(Collection<Condition> conditions) {
        for (Condition c : conditions) {
            if (c.noCombatTurn()) {
                return true;
            }
        }
        return false;
    }

    public static String getCharacterStatus(Collection<Condition> conditions) {
        StringBuilder bldr = new StringBuilder();
        for (Condition c : conditions) {
            bldr.append(c.getShortName() + " ");
        }
        return bldr.substring(0, bldr.length()-1);
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

    public void wasRemoved(Combatant combatant) {

    }

    public boolean removeAtEndOfCombat() {
        return false;
    }

    public void endOfDayTrigger(Model model, GameState state, Combatant comb) { }

    public void endOfCombatRoundTrigger(Model model, GameState state, Combatant comb) { }

    public int getAttackBonus() { return 0; }

    public abstract ConditionHelpDialog getHelpView(GameView view);

}
