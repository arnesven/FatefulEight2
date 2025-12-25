package model.states;

import control.GameExitedException;
import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.classes.*;
import model.combat.conditions.VampirismCondition;
import model.enemies.Enemy;
import model.items.Item;
import model.items.spells.Spell;
import model.races.Race;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import util.MyStrings;
import view.LogView;
import view.PartyAttitudesDialog;
import view.subviews.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public abstract class GameState implements GameStateConstants {

    private static final List<String> girlFirstNames = new ArrayList<>(COMMON_GIRL_FIRST_NAMES);
    private static final List<String> boyFirstNames = new ArrayList<>(COMMON_BOY_FIRST_NAMES);
    private static final List<String> lastNames = new ArrayList<>(COMMON_LAST_NAMES);

    private final Model model;

    public GameState(Model model) {
        this.model = model;
    }

    public abstract GameState run(Model model);

    protected Model getModel() { return model; }

    public void print(String s) {
        model.getLog().addAnimated(s);
    }

    public void printAlert(String s) {
        model.getLog().addAnimated(LogView.RED_COLOR + s + "\n" + LogView.DEFAULT_COLOR);
    }

    public void printQuote(String who, String quote) {
        model.getLog().addAnimated(who + ": " + LogView.YELLOW_COLOR + "\"" + quote + "\"\n" +
                LogView.DEFAULT_COLOR);
    }

    public void println(String s) {
        model.getLog().addAnimated(s + "\n");
    }

    public char singleCharInput() {
        model.getLog().acceptSingleCharInput();
        return internalInput().charAt(0);
    }

    public String lineInput() {
        model.getLog().acceptLineInput();
        return internalInput();
    }

    public void waitForReturn(boolean stopForSpell) {
        model.getLog().waitForReturn();
        internalInput(stopForSpell);
    }

    public void waitForReturn() {
        waitForReturn(false);
    }

    public void waitForReturnSilently(boolean stopForSpell) {
        model.getLog().waitForReturnSilently();
        internalInput(stopForSpell);
    }

    public void waitForReturnSilently() {
        waitForReturnSilently(false);
    }

    public boolean yesNoInput() {
        while (true) {
            model.getLog().acceptLineInput();
            String response = internalInput().toLowerCase();
            if (response.equals("y") || response.equals("yes")) {
                return true;
            } else if (response.equals("n") || response.equals("no")) {
                return false;
            }
        }
    }

    private String internalInput(boolean stopForSpell) {
        while (!model.gameExited()) {
            if (model.getLog().inputReady()) {
                return model.getLog().getInput();
            }
            if (stopForSpell) {
                model.getSpellHandler().pollCastSpells();
            }
            sleep(20);
        }
        throw new GameExitedException();
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String internalInput() {
        return internalInput(false);
    }

    public static void setCurrentTerrainSubview(Model model) {
        SubView nextSubView;
        if (showOnRoad(model)) {
            nextSubView = OnTheRoadSubView.instance;
        } else {
            nextSubView = model.getCurrentHex().getImageSubView(model);
        }
        if (model.getSubView() != nextSubView) {
            CollapsingTransition.transition(model, nextSubView);
        }
    }

    protected void stepToNextDay(Model model) {
        MyLists.forEach(model.getParty().getPartyMembers(),
                (GameCharacter gc) -> gc.conditionsEndOfDayTrigger(model, this));
        model.incrementDay();
    }

    protected static boolean showOnRoad(Model model) {
        return !model.getCurrentHex().hasLodging() &&
                model.getParty().isOnRoad() &&
                !model.getCurrentHex().inhibitOnRoadSubview();
    }

    protected int multipleOptionArrowMenu(Model model, int x, int y, List<String> optionList) {
        int[] selectedAction = new int[1];
        model.setSubView(new ArrowMenuSubView(model.getSubView(),
                optionList, x, y, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selectedAction[0] = cursorPos;
                model.setSubView(getPrevious());
            }
        });
        waitForReturnSilently(true);
        return selectedAction[0];
    }

    public void leaderSay(String line) {
        getModel().getParty().partyMemberSay(getModel(), getModel().getParty().getLeader(), line);
    }

    public void partyMemberSay(GameCharacter gc, String line) {
        getModel().getParty().partyMemberSay(getModel(), gc, line);
    }

    public void notLeaderSay(String line) {
        if (model.getParty().size() > 1) {
            GameCharacter gc = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(gc, line);
        }
    }

    public static String heOrSheCap(boolean gender) {
        return gender ? "She" : "He";
    }

    public static String heOrShe(boolean gender) {
        return gender ? "she" : "he";
    }

    public static String manOrWoman(boolean gender) {
        return manOrWomanCap(gender).toLowerCase();
    }

    public static String manOrWomanCap(boolean gender) {
        return gender ? "Woman" : "Man";
    }

    public static String boyOrGirl(boolean gender) {
        return boyOrGirlCap(gender).toLowerCase();
    }

    public static String boyOrGirlCap(boolean gender) {
        return gender ? "Girl" : "Boy";
    }

    public static String himOrHer(boolean gender) {
        return gender ? "her" : "him";
    }

    public static String hisOrHer(boolean gender) {
        return gender ? "her" : "his";
    }

    protected String meOrUs() { return model.getParty().size() == 1 ? "me":"us"; }

    protected String iOrWe() { return model.getParty().size() == 1 ? "I":"we"; }

    protected String iOrWeCap() { return model.getParty().size() == 1 ? "I":"We"; }

    protected String iveOrWeve() { return model.getParty().size() == 1 ? "I've":"we've"; }

    protected String iWasOrWeWere() { return model.getParty().size() == 1 ? "I was" : "we were"; }

    protected String imOrWere() { return model.getParty().size() == 1 ? "I'm":"we're"; }

    protected String imOrWereCap() { return model.getParty().size() == 1 ? "I'm":"We're"; }

    protected String myOrOur() { return model.getParty().size() == 1 ? "my":"our"; }

    protected void showPartyAttitudesSubView(Model model) {
        model.setSubView(new PartyAttitudesDialog(model));
        model.getTutorial().attitudes(model);
        waitForReturn();
    }

    public static double calculateAverageLevel(Model model) {
        double averageLevel = MyLists.doubleAccumulate(model.getParty().getPartyMembers(), GameCharacter::getLevel);
        averageLevel /= model.getParty().size();
        return averageLevel;
    }

    public static String randomFirstName(boolean gender) {
        if (gender) {
            return MyRandom.sample(COMMON_GIRL_FIRST_NAMES);
        }
        return MyRandom.sample(COMMON_BOY_FIRST_NAMES);
    }

    public static String randomLastName() {
        return MyRandom.sample(lastNames);
    }

    public static GameCharacter makeRandomCharacter(int level, Race race) {
        CharacterClass cls = randomClass();
        boolean gender = MyRandom.flipCoin();
        AdvancedAppearance portrait = PortraitSubView.makeRandomPortrait(cls, race, gender);
        String firstName = randomFirstName(gender);
        if (lastNames.isEmpty()) {
            lastNames.addAll(COMMON_LAST_NAMES);
        }
        String lastName = lastNames.remove(MyRandom.randInt(lastNames.size()));
        GameCharacter gc = new GameCharacter(firstName, lastName, race, cls, portrait,
                makeRandomClassSet(cls));
        gc.setLevel(level);
        return gc;
    }

    public static GameCharacter makeRandomCharacter(int level) {
        return makeRandomCharacter(level, Race.randomRace());
    }

    private static CharacterClass randomClass() {
        CharacterClass cls = null;
        do {
            cls = Classes.allClasses[MyRandom.randInt(Classes.allClasses.length)];
        } while (cls == Classes.None);
        return cls;
    }

    public static CharacterClass[] makeRandomClassSet(CharacterClass cls) {
        Set<CharacterClass> hashSet = new HashSet<>(List.of(cls));
        while (hashSet.size() < 4) {
            hashSet.add(randomClass());
        }
        List<CharacterClass> list = new ArrayList<>(hashSet);
        return new CharacterClass[]{list.get(0), list.get(1), list.get(2), list.get(3)};
    }

    public static int getSuggestedNumberOfEnemies(Model model, Enemy enemy) {
        int max = 24;
        if (enemy.getWidth() > 1) {
            max = max / enemy.getWidth();
        }
        return Math.min(max, Math.max(1, model.getParty().partyStrength() / (enemy).getThreat()));
    }

    public boolean randomSayIfPersonality(PersonalityTrait trait, List<GameCharacter> excluding, String line) {
        List<GameCharacter> candidates = new ArrayList<>(model.getParty().getPartyMembers());
        candidates.removeAll(model.getParty().getBench());
        candidates.removeAll(excluding);
        candidates.removeIf(gameCharacter -> !gameCharacter.hasPersonality(trait));
        if (candidates.isEmpty()) {
            return false;
        }
        GameCharacter speaker = MyRandom.sample(candidates);
        partyMemberSay(speaker, line);
        return true;
    }

    public MyPair<SkillCheckResult, GameCharacter> doPassiveSkillCheck(Skill skill, int difficulty, Skill bonusFromSkill) {
        SkillCheckResult result = null;
        difficulty = SkillChecks.adjustDifficulty(model, difficulty);
        List<GameCharacter> partyMembers = new ArrayList<>(model.getParty().getPartyMembers());
        partyMembers.removeAll(model.getParty().getBench());
        for (GameCharacter gc : partyMembers) {
            int bonus = 0;
            if (bonusFromSkill != null) {
                bonus = gc.getRankForSkill(bonusFromSkill);
            }
            result = gc.testSkillHidden(skill, difficulty, bonus);
            if (result.isSuccessful()) {
                return new MyPair<>(result, gc);
            }
        }
        return new MyPair<>(result, null);
    }

    public MyPair<SkillCheckResult, GameCharacter> doPassiveSkillCheck(Skill skill, int difficulty) {
        return doPassiveSkillCheck(skill, difficulty, null);
    }

    public static boolean partyIsCreepy(Model model) {
        int creepyness = model.getParty().getNotoriety();
        if (model.getParty().getLeader().hasCondition(VampirismCondition.class)) {
            VampirismCondition cond = (VampirismCondition) model.getParty().getLeader().getCondition(VampirismCondition.class);
            if (cond.getStage() > 0) {
                creepyness += cond.getStage() * 100;
            }
        }
        creepyness += MyLists.intAccumulate(model.getParty().getPartyMembers(),
                gc -> gc.hasCondition(VampirismCondition.class)
                        ? 20 * ((VampirismCondition)gc.getCondition(VampirismCondition.class)).getStage()
                        : 0);
        return creepyness >= 100;
    }

    public <E> void waitUntil(E arg, Predicate<E> test, boolean withSpell) {
        while (!test.test(arg) && !getModel().gameExited()) {
            if (withSpell) {
                model.getSpellHandler().pollCastSpells();
            }
            sleep(20);
        }
        if (model.gameExited()) {
            throw new GameExitedException();
        }
    }

    public <E> void waitUntil(E arg, Predicate<E> test) {
        waitUntil(arg, test, false);
    }

    public <E> void waitUntilOrSpell(E arg, Predicate<E> test) {
        waitUntil(arg, test, true);
    }

    public void delay(int millis) {
        sleep(millis);
        if (model.gameExited()) {
            throw new GameExitedException();
        }
    }

    public void completeAchievement(String key) {
        if (!model.getAchievements().isCompleted(key, getModel())) {
            model.getAchievements().setCompleted(key);
            String name = model.getAchievements().getAchievement(key).getName();
            model.getLog().addAnimated(LogView.GOLD_COLOR + "You have unlocked an achievement '" +
                    name + "'!\n" + LogView.DEFAULT_COLOR);
        }
    }
}
