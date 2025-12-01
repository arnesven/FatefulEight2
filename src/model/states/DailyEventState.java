package model.states;

import model.GameStatistics;
import model.Model;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.combat.CombatAdvantage;
import model.items.spells.ResurrectSpell;
import model.items.spells.Spell;
import model.states.events.GuideData;
import view.combat.CombatTheme;
import view.combat.CaveTheme;
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

    private boolean fledCombat = false;
    private PortraitSubView portraitSubView;

    public DailyEventState(Model model) {
        super(model);
    }

    protected abstract void doEvent(Model model);

    @Override
    public final GameState run(Model model) {
        if (model.getParty().isWipedOut()) {
            return new GameOverState(model);
        }
        doStartOfEventHook(model);
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
        return doEndOfEventHook(model);
    }

    protected void doStartOfEventHook(Model model) { }

    protected GameState doEndOfEventHook(Model model) {
        if (MyRandom.rollD10() <= model.getParty().getPartyMembers().size() - 5 && allowPartyEvent()) {
            return WorldHex.generatePartyEvent(model);
        }
        model.setTimeOfDay(TimeOfDay.EVENING);
        return getEveningState(model);
    }

    protected boolean allowPartyEvent() {
        return true;
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

    public void runCombat(List<Enemy> enemies, CombatTheme theme, boolean fleeingEnabled, CombatAdvantage combatAdvantage,
                          List<GameCharacter> allies) {
        CombatEvent combat = new CombatEvent(getModel(), enemies, theme, fleeingEnabled, combatAdvantage);
        combat.addExtraLoot(getExtraCombatLoot(getModel()));
        combat.addAllies(allies);
        combat.run(getModel());
        fledCombat = combat.fled();
    }

    protected List<CombatLoot> getExtraCombatLoot(Model model) {
        return new ArrayList<>();
    }

    protected void runCombat(List<Enemy> enemies) {
        runCombat(enemies, defaultCombatTheme(getModel()), true, CombatAdvantage.Neither, new ArrayList<>());
    }

    private CombatTheme defaultCombatTheme(Model model) {
        if (model.isInCaveSystem()) {
            return new CaveTheme();
        }
        return model.getCurrentHex().getCombatTheme();
    }

    protected void runCombat(List<Enemy> enemies, boolean fleeingEnabled) {
        runCombat(enemies, defaultCombatTheme(getModel()), fleeingEnabled, CombatAdvantage.Neither, new ArrayList<>());
    }

    protected void runCombat(List<Enemy> enemies, CombatTheme theme, boolean fleeingEnabled) {
        runCombat(enemies, theme, fleeingEnabled, CombatAdvantage.Neither, new ArrayList<>());
    }

    protected void runSurpriseCombat(List<Enemy> enemies, CombatTheme theme, boolean fleeingEnabled) {
        runCombat(enemies, theme, fleeingEnabled, CombatAdvantage.Party, new ArrayList<>());
    }

    protected void runAmbushCombat(List<Enemy> enemies, CombatTheme theme, boolean fleeingEnabled) {
        runCombat(enemies, theme, fleeingEnabled, CombatAdvantage.Enemies, new ArrayList<>());
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

        for (GameCharacter gc : new ArrayList<>(toRemove)) {
            if (!didResurrect(model, this, gc)) {
                model.getParty().remove(gc, !abandonEquipment, false, 0);
            } else {
                toRemove.remove(gc);
            }
        }
        if (toRemove.isEmpty()) {
            return;
        }
        String names = MyLists.commaAndJoin(toRemove, GameCharacter::getName);
        printAlert(names + " " + (toRemove.size()>1?"have":"has")+ " died.");
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
        List<String> options = new ArrayList<>();
        RevivingElixir revive = null;
        for (Potion p : model.getParty().getInventory().getPotions()) {
            if (p instanceof RevivingElixir) {
                revive = (RevivingElixir) p;
                options.add(("Use " + p.getName()));
            }
        }
        ResurrectSpell resSpell = null;
        for (Spell sp : model.getParty().getSpells()) {
            if (sp instanceof ResurrectSpell) {
                resSpell = (ResurrectSpell) sp;
                options.add("Cast " + resSpell.getName());
            }
        }

        if (revive == null && resSpell == null) {
            return false;
        }

        if (resSpell == null) {
            return RevivingElixir.reviveWithElixir(model, event, gc, revive);
        } else if (revive == null) {
            return ResurrectSpell.useDuringEvent(model, event, gc, resSpell);
        }

        options.add("No thanks");
        event.println("How would you like to revive " + gc.getName() + "?");
        int chosen = event.multipleOptionArrowMenu(model, 24, 24, options);
        if (options.get(chosen).contains("Use")) {
            return RevivingElixir.reviveWithElixir(model, event, gc, revive);
        }
        if (options.get(chosen).contains("Cast")) {
            return ResurrectSpell.useDuringEvent(model, event, gc, resSpell);
        }
        return false;
    }

    protected void forcedMovement(Model model, List<Point> path) {
        MapSubView mapSubView = new MapSubView(model);
        mapSubView.drawAvatarEnabled(false);
        CollapsingTransition.transition(model, mapSubView);
        Point currentPos = model.getParty().getPosition();
        for (int i = 1; i < path.size(); ++i) {
            Point destination = path.get(i);
            if (i == path.size()-1) {
                model.exitCaveSystem(false);
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
        model.getParty().setOnRoad(false);
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
            portraitSubView.dispose();
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

    protected void forcePortraitEyes(boolean closed) {
        portraitSubView.forceEyesClosed(closed);
    }

    protected static int calculatePartyAlignment(Model model, DailyEventState event) {
        model.getTutorial().alignment(model);
        int sum = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            int modifier = 0;
            if (Classes.ALIGNMENT.containsKey(gc.getCharClass().id())) {
                modifier = Classes.ALIGNMENT.get(gc.getCharClass().id());
            }
            if (gc.getCharClass().id() != Classes.None.id()) {
                event.println("... " + gc.getFirstName() + " is a " + gc.getCharClass().getFullName() + ": " + MyStrings.withPlus(modifier));
                sum += modifier;
            }
        }
        event.println("... Total party alignment: " + MyStrings.withPlus(sum));
        return sum;
    }

    public static int getPartyAlignment(Model model) {
        int sum = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            int modifier = 0;
            if (Classes.ALIGNMENT.containsKey(gc.getCharClass().id())) {
                modifier = Classes.ALIGNMENT.get(gc.getCharClass().id());
            }
            sum += modifier;
        }
        return sum;
    }

    protected GameCharacter makeRandomCharacter() {
        int level = (int)Math.ceil(GameState.calculateAverageLevel(getModel()));
        return makeRandomCharacter(level);
    }

    protected void possiblyGetHorsesAfterCombat(String enemy, int mostNumberOfHorses) {
        if (!haveFledCombat() && !getModel().getParty().isWipedOut()) {
            int numberOfHorses = MyRandom.randInt(mostNumberOfHorses+1);
            if (numberOfHorses > 0) {
                if (numberOfHorses == 1) {
                    println("The " + enemy + " had a horse which you happily take over ownership of.");
                } else {
                    println("The " + enemy + " had some horses which you happily take ownership of.");
                }
                for (int i = numberOfHorses; i > 0; --i) {
                    Horse horse = HorseHandler.generateHorse();
                    getModel().getParty().getHorseHandler().addHorse(horse);
                    println("The party got a " + horse.getName() + ".");
                }
            }
        }
    }

    public boolean isGuidable() {
        return getGuideData() != null;
    }


    public GuideData getGuideData() {
        return null;
    }

    public String getDistantDescription() {
        return null;
    }

    public boolean exclusiveToOriginalWorld() {
        return false;
    }
}
