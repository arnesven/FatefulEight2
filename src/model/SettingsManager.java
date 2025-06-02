package model;

import util.Arithmetics;
import view.widget.TopText;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsManager implements Serializable {

    public enum LogSpeed {
        SLOWER, SLOW, FAST, FASTER
    }

    private boolean autosave = true;
    private LogSpeed logSpeed = LogSpeed.SLOW;
    private LogSpeed combatLogSpeed = LogSpeed.SLOW;
    private LogSpeed movementSpeed = LogSpeed.SLOW;
    private boolean levelUpSummary = true;
    private boolean alwaysRide = false;
    private final Map<String, Boolean> miscFlags = new HashMap<>();
    private final Map<String, Integer> miscCounters = new HashMap<>();
    private boolean animateDieRolls = true;
    private int gameDifficulty = 1;
    public static final int MAX_DIFFICULTY = 3;

    public SettingsManager() {
        miscFlags.put(TopText.TIME_OF_DAY_SETTINGS_FLAG, true);
        miscFlags.put(TopText.GOLD_SETTINGS_FLAG, true);
        miscFlags.put(TopText.OBOLS_SETTINGS_FLAG, false);
        miscFlags.put(TopText.FOOD_SETTINGS_FLAG, true);
        miscFlags.put(TopText.WEIGHT_SETTINGS_FLAG, true);
        miscFlags.put(TopText.CARRYING_CAPACITY_SETTINGS_FLAG, true);
        miscFlags.put(TopText.HORSE_SETTINGS_FLAG, true);
        miscFlags.put(TopText.ALIGNMENT_SETTINGS_FLAG, true);
        miscFlags.put(TopText.NOTORIETY_SETTINGS_FLAG, true);
        miscFlags.put(TopText.REPUTATION_SETTINGS_FLAG, true);
        miscFlags.put(TopText.INGREDIENTS_SETTINGS_FLAG, false);
        miscFlags.put(TopText.MATERIALS_SETTINGS_FLAG, false);
        miscFlags.put(TopText.LOCKPICKS_SETTINGS_FLAG, false);
        miscFlags.put(TopText.KEY_REMINDERS_SETTINGS_FLAG, true);
    }

    public void toggleAutosave() {
        if (gameDifficulty != MAX_DIFFICULTY) {
            autosave = !autosave;
        }
    }

    public boolean autosaveEnabled() {
        return autosave;
    }

    public LogSpeed getLogSpeed() {
        return logSpeed;
    }

    public LogSpeed getCombatLogSpeed() { return combatLogSpeed; }

    public static String logSpeedAsText(LogSpeed logSpeed) {
        switch (logSpeed) {
            case FAST:
                return "FAST";
            case FASTER:
                return "FASTER";
            case SLOW:
                return "SLOW";
            default:
                return "SLOWER";
        }
    }

    public void toggleLogSpeed() {
        logSpeed = cycleSpeed(logSpeed);
    }

    public void toggleCombatLogSpeed() {
        combatLogSpeed = cycleSpeed(combatLogSpeed);
    }

    public void toggleMovementSpeed() {
        movementSpeed = cycleSpeed(movementSpeed);
    }

    private LogSpeed cycleSpeed(LogSpeed logSpeed) {
        if (logSpeed == LogSpeed.FAST) {
            return LogSpeed.FASTER;
        }
        if (logSpeed == LogSpeed.SLOW) {
            return LogSpeed.FAST;
        }
        if (logSpeed == LogSpeed.SLOWER) {
            return LogSpeed.SLOW;
        }
        return LogSpeed.SLOWER;
    }

    public static boolean tutorialEnabled(Model model) {
        return model.getTutorial().isTutorialEnabled();
    }

    public static void toggleTutorial(Model model) {
        model.getTutorial().setTutorialEnabled(!model.getTutorial().isTutorialEnabled());
    }

    public boolean levelUpSummaryEnabled() {
        return levelUpSummary;
    }

    public void toggleLevelUpSummary() {
        levelUpSummary = !levelUpSummary;
    }

    public boolean alwaysRide() { return alwaysRide; }

    public void toggleAlwaysRide() { alwaysRide = !alwaysRide; }

    public Map<String, Boolean> getMiscFlags() {
        return miscFlags;
    }

    public Map<String, Integer> getMiscCounters() { return miscCounters; }

    public LogSpeed getMovementSpeed() {
        return movementSpeed;
    }

    public boolean animateDieRollsEnabled() {
        return animateDieRolls;
    }

    public void toggleAnimateDieRolls() {
        animateDieRolls = !animateDieRolls;
    }

    public int getGameDifficulty() {
        return gameDifficulty;
    }

    public String getGameDifficultyString() {
        switch (gameDifficulty) {
            case 0 :
                return "EASY";
            case 1:
                return "NORMAL";
            case 2:
                return "HARD";
            case MAX_DIFFICULTY:
                return "IMPOS";
        }
        throw new IllegalStateException("Illegal game difficulty: " + gameDifficulty);
    }

    public void cycleGameDifficulty() {
        gameDifficulty = Arithmetics.incrementWithWrap(gameDifficulty, MAX_DIFFICULTY+1);
        if (gameDifficulty == MAX_DIFFICULTY && autosave) {
            autosave = false;
        }
    }

    public void setGameDifficulty(int difficulty) {
        this.gameDifficulty = difficulty;
        if (gameDifficulty == MAX_DIFFICULTY && autosave) {
            autosave = false;
        }
    }
}
