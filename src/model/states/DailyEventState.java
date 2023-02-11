package model.states;

import model.Model;
import model.TimeOfDay;
import model.characters.GameCharacter;
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

    protected boolean haveFledCombat() {
        return fledCombat;
    }

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
}
