package model.states;

import model.Model;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.combat.PoisonCondition;
import model.enemies.Enemy;
import model.map.World;
import model.races.Race;
import model.states.events.ConstableEvent;
import util.MyStrings;
import view.sprites.CalloutSprite;
import view.subviews.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class DailyEventState extends GameState {
    private static final Map<Integer, Integer> alignmentMap = makePartyAlignmentMap();;
    private boolean fledCombat = false;
    private PortraitSubView portraitSubView;

    public DailyEventState(Model model) {
        super(model);
    }

    @Override
    public final GameState run(Model model) {
        doEvent(model);
        removePortraitSubView(model);
        if (model.getParty().isWipedOut()) {
            return new GameOverState(model);
        }
        if (haveFledCombat()) {
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
    public void doTheEvent(Model model) { doEvent(model); }

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
        runCombat(enemies, getModel().getCurrentHex().getCombatTheme(), true);
    }

    protected void runCombat(List<Enemy> enemies, boolean fleeingEnabled) {
        runCombat(enemies, getModel().getCurrentHex().getCombatTheme(), fleeingEnabled);
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

    protected void forcedMovement(Model model, List<Point> path) {
        MapSubView mapSubView = new MapSubView(model);
        mapSubView.drawAvatarEnabled(false);
        CollapsingTransition.transition(model, mapSubView);
        Point currentPos = model.getParty().getPosition();
        for (int i = 1; i < path.size(); ++i) {
            Point destination = path.get(i);
            if (i == path.size()-1) {
                model.exitCaveSystem();
            }
            mapSubView.addMovementAnimation(
                    model.getParty().getLeader().getAvatarSprite(),
                    World.translateToScreen(currentPos, model.getParty().getPosition(), MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES),
                    World.translateToScreen(destination, model.getParty().getPosition(), MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES));
            mapSubView.waitForAnimation();
            mapSubView.removeMovementAnimation();
            model.getParty().setPosition(destination);
            currentPos = destination;
        }
        model.getCurrentHex().travelFrom(model);
        model.getParty().setPosition(currentPos);
        model.getCurrentHex().travelTo(model);
    }

    protected void showRandomPortrait(Model model, CharacterClass cls, Race race, String portraitName) {
        if (portraitSubView != null) {
            removePortraitSubView(model);
        }
        portraitSubView = new PortraitSubView(model.getSubView(), cls, race, portraitName);
        model.setSubView(portraitSubView);
    }

    protected void showRandomPortrait(Model model, CharacterClass cls, String portraitName) {
        showRandomPortrait(model, cls, Race.ALL, portraitName);
    }

    protected void showSilhouettePortrait(Model model, String name) {
        portraitSubView = new PortraitSubView(model.getSubView(), PortraitSubView.SILHOUETTE, name);
        model.setSubView(portraitSubView);
    }

    protected void removePortraitSubView(Model model) {
        if (portraitSubView != null) {
            model.setSubView(portraitSubView.getPreviousSubView());
        }
        portraitSubView = null;
    }

    protected void portraitSay(Model model, String line) {
        portraitSubView.portraitSay(model, this, line);
    }

    protected boolean getPortraitGender() {
        return portraitSubView.getPortraitGender();
    }

    protected static int getPartyAlignment(Model model, DailyEventState event) {
        int sum = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            int modifier = 0;
            if (alignmentMap.containsKey(gc.getCharClass().id())) {
                modifier = alignmentMap.get(gc.getCharClass().id());
            }
            event.println("... " + gc.getFirstName() + " is a " + gc.getCharClass().getFullName() + ": " + MyStrings.withPlus(modifier));
            sum += modifier;
        }
        event.println("... Bonus for reputation: " + MyStrings.withPlus(model.getParty().getReputation()));
        event.println("... Total party alignment: " + MyStrings.withPlus(sum));
        return sum;
    }

    private static Map<Integer, Integer> makePartyAlignmentMap() {
        Map<Integer, Integer> classMap = new HashMap<>();
        classMap.put(Classes.ASN.id(), -2);
        classMap.put(Classes.BKN.id(), -2);
        classMap.put(Classes.THF.id(), -1);
        classMap.put(Classes.SOR.id(), -1);
        classMap.put(Classes.WIT.id(), -1);
        classMap.put(Classes.BBN.id(), -1);
        classMap.put(Classes.SPY.id(), -1);

        classMap.put(Classes.PRI.id(), +1);
        classMap.put(Classes.PAL.id(), +1);
        classMap.put(Classes.NOB.id(), +1);
        return classMap;
    }
}
