package model;

import view.widget.TopText;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SettingsManager implements Serializable {

    public enum LogSpeed {
        SLOW, FAST, FASTER
    }

    private boolean autosave = true;
    private LogSpeed logSpeed = LogSpeed.FAST;
    private LogSpeed combatLogSpeed = LogSpeed.FAST;
    private boolean levelUpSummary = true;
    private boolean alwaysRide = false;
    private Map<String, Boolean> miscFlags = new HashMap<>();

    public SettingsManager() {
        miscFlags.put(TopText.GOLD_SETTINGS_FLAG, true);
        miscFlags.put(TopText.OBOLS_SETTINGS_FLAG, false);
        miscFlags.put(TopText.FOOD_SETTINGS_FLAG, true);
        miscFlags.put(TopText.HORSE_SETTINGS_FLAG, true);
        miscFlags.put(TopText.ALIGNMENT_SETTINGS_FLAG, true);
        miscFlags.put(TopText.NOTORIETY_SETTINGS_FLAG, true);
        miscFlags.put(TopText.REPUTATION_SETTINGS_FLAG, true);
        miscFlags.put(TopText.MATERIALS_SETTINGS_FLAG, false);
        miscFlags.put(TopText.INGREDIENTS_SETTINGS_FLAG, false);
        miscFlags.put(TopText.KEY_REMINDERS_SETTINGS_FLAG, true);
    }

    public void toggleAutosave() {
        autosave = !autosave;
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
            default:
                return "SLOW";
        }
    }

    public void toggleLogSpeed() {
        logSpeed = cycleSpeed(logSpeed);
    }

    public void toggleCombatLogSpeed() {
        combatLogSpeed = cycleSpeed(combatLogSpeed);
    }

    private LogSpeed cycleSpeed(LogSpeed logSpeed) {
        if (logSpeed == LogSpeed.FAST) {
            return LogSpeed.FASTER;
        }
        if (logSpeed == LogSpeed.SLOW) {
            return LogSpeed.FAST;
        }
        return LogSpeed.SLOW;
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
}
