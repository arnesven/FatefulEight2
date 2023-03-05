package model.states;

import model.Model;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.combat.PoisonCondition;
import model.enemies.Enemy;
import model.races.Race;
import view.subviews.CombatTheme;
import view.subviews.GrassCombatTheme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DailyEventState extends GameState {
    private boolean fledCombat = false;

    public DailyEventState(Model model) {
        super(model);
    }

    @Override
    public final GameState run(Model model) {
        doEvent(model);
        if (model.getParty().isWipedOut()) {
            return new GameOverState(model);
        }
        if (fledCombat) {
            return new RunAwayState(model);
        }
        model.setTimeOfDay(TimeOfDay.EVENING);
        return getEveningState(model);
    }

    protected GameState getEveningState(Model model) {
        return model.getCurrentHex().getEveningState(model, isFreeLodging(), isFreeRations());
    }

    protected boolean isFreeRations() {
        return false;
    }

    protected boolean isFreeLodging() {
        return false;
    }

    protected abstract void doEvent(Model model);

    protected void adventurerWhoMayJoin(Model model, Race race) {
        List<GameCharacter> list = model.getAvailableCharactersOfRace(race);
        if (list.isEmpty()) {
            println("n old friend with whom you exchange a few stories. You then part ways.");
        } else {
            println("n adventurer who offers to join your party.");
            Collections.shuffle(list);
            while (list.size() > 1) {
                list.remove(0);
            }
            list.get(0).setRandomStartingClass();
            RecruitState recruitState = new RecruitState(model, list);
            recruitState.run(model);
        }
    }

    protected void runCombat(List<Enemy> enemies, CombatTheme theme, boolean fleeingEnabled) {
        CombatEvent combat = new CombatEvent(getModel(), enemies, theme, fleeingEnabled);
        combat.run(getModel());
        fledCombat = combat.fled();
    }

    protected void runCombat(List<Enemy> enemies) {
        runCombat(enemies, new GrassCombatTheme(), true);
    }

    public boolean haveFledCombat() {
        return fledCombat;
    }
    protected void setFledCombat(boolean b) { fledCombat = b; }

    protected void removeKilledPartyMembers(Model model, boolean abandonEquipment) {
        List<GameCharacter> toRemove = new ArrayList<>();
        StringBuffer buf = new StringBuffer();
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.isDead()) {
                toRemove.add(gc);
                buf.append(gc.getFullName() + ", ");
            }
        }
        if (toRemove.isEmpty()) {
            return;
        }

        for (GameCharacter gc : toRemove) {
            model.getParty().remove(gc, !abandonEquipment, false, 0);
        }
        print(buf.toString().substring(0, buf.length()-2) + " has died.");
        if (!abandonEquipment && model.getParty().size() > 0) {
            println(" you bury them and collect the equipment.");
        } else {
            println("");
        }
    }

    public static void characterDies(Model model, GameState event, GameCharacter gc, String reason) {
        boolean wasLeader = gc.isLeader();
        event.println(gc.getName() + reason);
        model.getParty().remove(gc, false, false, 0);
        if (model.getParty().size() == 0) {
            event.println("Your last party member has been eliminated. Press any key to continue.");
            event.waitForReturn();
            model.setGameOver(true);
        } else {
            model.getParty().randomPartyMemberSay(model, List.of(gc.getFirstName().toUpperCase() + "!!!"));
            model.getParty().randomPartyMemberSay(model, List.of("..."));
            model.getParty().randomPartyMemberSay(model, List.of("Gone! " + gc.getFirstName() + " is gone!"));
            if (wasLeader) {
                event.println(model.getParty().getLeader().getName() + " is now the new leader of the party.");
            }
        }
    }

    protected String heOrSheCap(boolean gender) {
        return gender ? "She" : "He";
    }

    protected String heOrShe(boolean gender) {
        return gender ? "she" : "he";
    }

    protected String himOrHer(boolean gender) {
        return gender ? "her" : "him";
    }

    protected String hisOrHer(boolean gender) {
        return gender ? "her" : "his";
    }
}
