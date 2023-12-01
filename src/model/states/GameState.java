package model.states;

import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.combat.Condition;
import model.combat.PoisonCondition;
import model.enemies.ElfEnemy;
import model.enemies.Enemy;
import model.items.spells.Spell;
import model.races.Race;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.LogView;
import view.PartyAttitudesDialog;
import view.subviews.*;

import java.util.ArrayList;
import java.util.List;

public abstract class GameState {

    public static final List<String> COMMON_GIRL_FIRST_NAMES = List.of("Bella", "Steffi", "Ronya", "Felixa",
            "Ipona", "Esmeralda", "Gemma", "Petra", "Sinorin", "Adalia", "Cormona", "Sheila", "Dora",
            "Visenna", "Lara", "Gueniviere", "Cally", "Tessa", "Mandy", "Bianca", "Miranda",
            "Helga", "Goldy", "Emma", "Carly", "Jessica");
    public static final List<String> COMMON_BOY_FIRST_NAMES = List.of("Golbert", "Voldo", "Maxim", "Nestor",
            "Karg", "Tobert", "Roger", "Sammy", "Oleg", "Trevor", "Quellic", "Ben", "Ivan", "Feodor", "Stig",
            "Ralbert", "Rastigan", "Enoch", "Roy", "Georgi", "Leonard", "Albert", "Stanley", "Johnny", "Horace",
            "Derric", "Felix", "Igor", "Marcus", "Stig", "Hubert");
    public static final List<String> COMMON_LAST_NAMES = List.of("Wildfeather", "Cleareyes", "Al-Zaman",
            "Gerson", "Essex", "Overhill", "Sloch", "Petty", "Inderfelt", "Sharptooth", "Zeltic", "Hightower",
            "Edelweiss", "Eastwood", "Hardwater", "Azure", "Stormfist", "Samuelesen", "Haldic");
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

    public void waitForReturnSilently() {
        model.getLog().waitForReturnSilently();
        internalInput();
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
            if (stopForSpell && model.getSpellHandler().spellReady()) {
                MyPair<Spell, GameCharacter> pair = model.getSpellHandler().getCastSpell();
                throw new SpellCastException(pair.first, pair.second);
            }
            sleep();
        }
        System.exit(0);
        throw new IllegalStateException("Program failed to exit");
    }

    protected void sleep() {
        try {
            Thread.sleep(20);
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
            nextSubView = model.getCurrentHex().getImageSubView();
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
        waitForReturnSilently();
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

    public static GameCharacter makeRandomCharacter(int level) {
        CharacterClass cls = randomClass();
        Race race = Race.randomRace();
        boolean gender = MyRandom.flipCoin();
        AdvancedAppearance portrait = PortraitSubView.makeRandomPortrait(cls, race, gender);
        String firstName = randomFirstName(gender);
        String lastName = lastNames.remove(MyRandom.randInt(lastNames.size()));
        GameCharacter gc = new GameCharacter(firstName, lastName, race, cls, portrait,
                makeRandomClassSet(cls));
        gc.setLevel(level);
        return gc;
    }

    private static CharacterClass randomClass() {
        CharacterClass cls = null;
        do {
            cls = Classes.allClasses[MyRandom.randInt(Classes.allClasses.length)];
        } while (cls == Classes.None);
        return cls;
    }

    private static CharacterClass[] makeRandomClassSet(CharacterClass cls) {
        return new CharacterClass[]{cls, randomClass(), randomClass(), randomClass()};
    }

    public static int getSuggestedNumberOfEnemies(Model model, Enemy enemy) {
        return Math.max(1, model.getParty().partyStrength() / (enemy).getThreat());
    }
}
