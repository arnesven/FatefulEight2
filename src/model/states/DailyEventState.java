package model.states;

import model.Model;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.characters.TamedDragonCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import view.subviews.CaveTheme;
import model.combat.loot.CombatLoot;
import model.enemies.Enemy;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.items.potions.Potion;
import model.items.potions.RevivingElixir;
import model.map.WorldHex;
import model.races.Race;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;
import view.subviews.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class DailyEventState extends GameState {
    private static final Map<Integer, Integer> alignmentMap = makePartyAlignmentMap();
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
        if (allowCheckForFlee()) {
            if (haveFledCombat()) {
                return new RunAwayState(model);
            }
        }
        if (MyRandom.rollD10() <= model.getParty().getPartyMembers().size() - 4) {
            return WorldHex.generatePartyEvent(model);
        }
        model.setTimeOfDay(TimeOfDay.EVENING);
        return getEveningState(model);
    }

    protected boolean allowCheckForFlee() {
        return true;
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

    protected void runCombat(List<Enemy> enemies, CombatTheme theme, boolean fleeingEnabled, boolean isAmbush) {
        CombatEvent combat = new CombatEvent(getModel(), enemies, theme, fleeingEnabled, isAmbush);
        combat.addExtraLoot(getExtraCombatLoot(getModel()));
        combat.run(getModel());
        fledCombat = combat.fled();
    }

    protected List<CombatLoot> getExtraCombatLoot(Model model) {
        return new ArrayList<>();
    }

    protected void runCombat(List<Enemy> enemies) {
        runCombat(enemies, defaultCombatTheme(getModel()), true, false);
    }

    private CombatTheme defaultCombatTheme(Model model) {
        if (model.isInCaveSystem()) {
            return new CaveTheme();
        }
        return model.getCurrentHex().getCombatTheme();
    }

    protected void runCombat(List<Enemy> enemies, boolean fleeingEnabled) {
        runCombat(enemies, defaultCombatTheme(getModel()), fleeingEnabled, false);
    }

    protected void runCombat(List<Enemy> enemies, CombatTheme theme, boolean fleeingEnabled) {
        runCombat(enemies, theme, fleeingEnabled, false);
    }

    protected void runAmbushCombat(List<Enemy> enemies, CombatTheme theme, boolean fleeingEnabled) {
        runCombat(enemies, theme, fleeingEnabled, true);
    }

    public boolean haveFledCombat() {
        return fledCombat;
    }
    protected void setFledCombat(boolean b) { fledCombat = b; }

    protected void removeKilledPartyMembers(Model model, boolean abandonEquipment) {
        List<GameCharacter> toRemove = MyLists.filter(model.getParty().getPartyMembers(), GameCharacter::isDead);
        if (toRemove.isEmpty()) {
            return;
        }

        StringBuffer buf = new StringBuffer();
        for (GameCharacter gc : toRemove) {
            if (!didResurrect(model, this, gc)) {
                model.getParty().remove(gc, !abandonEquipment, false, 0);
                buf.append(gc.getFullName() + ", ");
            }
        }
        if (buf.toString().equals("")) {
            return;
        }
        printAlert(buf.toString().substring(0, buf.length()-2) + " has died.");
        if (!abandonEquipment && model.getParty().size() > 0) {
            println("You bury them and collect the equipment.");
        } else {
            println("");
        }
    }

    public static void characterDies(Model model, GameState event, GameCharacter gc, String reason, boolean offerResurrect) {
        boolean wasLeader = gc.isLeader();
        event.println(gc.getName() + reason);
        if (offerResurrect && model.getParty().size() > 1 && didResurrect(model, event, gc)) {
            return;
        }
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

    public static boolean didResurrect(Model model, GameState event, GameCharacter gc) {
        RevivingElixir revive = null;
        for (Potion p : model.getParty().getInventory().getPotions()) {
            if (p instanceof RevivingElixir) {
                revive = (RevivingElixir) p;
            }
        }
        if (revive == null) {
            return false;
        }
        event.print("Do you want to use " + revive.getName() + " to revive " + gc.getName() + "? (Y/N) ");
        if (!event.yesNoInput()) {
            return false;
        }
        model.getParty().getInventory().remove(revive);
        String result = revive.useYourself(model, gc);
        event.println(result);
        return true;
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
                    model.getWorld().translateToScreen(currentPos, model.getParty().getPosition(), MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES),
                    model.getWorld().translateToScreen(destination, model.getParty().getPosition(), MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES));
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

    public void setPortraitSubView(DailyEventState event) {
        portraitSubView = event.portraitSubView;
    }

    protected void showRandomPortrait(Model model, CharacterClass cls, String portraitName) {
        showRandomPortrait(model, cls, Race.ALL, portraitName);
    }

    protected void showSilhouettePortrait(Model model, String name) {
        portraitSubView = new PortraitSubView(model.getSubView(), PortraitSubView.SILHOUETTE, name);
        model.setSubView(portraitSubView);
    }

    protected void showExplicitPortrait(Model model, CharacterAppearance appearance, String name) {
        portraitSubView = new PortraitSubView(model.getSubView(), appearance, name);
        model.setSubView(portraitSubView);
    }

    protected void removePortraitSubView(Model model) {
        if (portraitSubView != null) {
            model.setSubView(portraitSubView.getPreviousSubView());
        }
        portraitSubView = null;
    }

    protected void portraitSay(String line) {
        portraitSubView.portraitSay(getModel(), this, line);
        getModel().getLog().waitForAnimationToFinish();
    }

    protected boolean getPortraitGender() {
        return portraitSubView.getPortraitGender();
    }

    protected static int calculatePartyAlignment(Model model, DailyEventState event) {
        model.getTutorial().alignment(model);
        int sum = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            int modifier = 0;
            if (alignmentMap.containsKey(gc.getCharClass().id())) {
                modifier = alignmentMap.get(gc.getCharClass().id());
            }
            event.println("... " + gc.getFirstName() + " is a " + gc.getCharClass().getFullName() + ": " + MyStrings.withPlus(modifier));
            sum += modifier;
        }
        event.println("... Total party alignment: " + MyStrings.withPlus(sum));
        return sum;
    }

    public static int getPartyAlignment(Model model) {
        int sum = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            int modifier = 0;
            if (alignmentMap.containsKey(gc.getCharClass().id())) {
                modifier = alignmentMap.get(gc.getCharClass().id());
            }
            sum += modifier;
        }
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

    protected GameCharacter makeRandomCharacter() {
        int level = (int)Math.ceil(GameState.calculateAverageLevel(getModel()));
        return makeRandomCharacter(level);
    }

    protected void possiblyGetHorsesAfterCombat(String enemy, int mostNumberOfHorses) {
        if (!haveFledCombat()) {
            int numberOfHorses = MyRandom.randInt(mostNumberOfHorses+1);
            if (numberOfHorses > 0) {
                if (numberOfHorses == 1) {
                    println("The " + enemy + " had a horse which you happily take over ownership of.");
                } else {
                    println("The " + enemy + " had some horses which you happily take over ownership of.");
                }
                for (int i = numberOfHorses; i > 0; --i) {
                    Horse horse = HorseHandler.generateHorse();
                    getModel().getParty().getHorseHandler().addHorse(horse);
                    println("The party got a " + horse.getName() + ".");
                }
            }
        }
    }
}
