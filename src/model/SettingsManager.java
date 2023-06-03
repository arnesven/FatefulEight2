package model;

import java.io.Serializable;

public class SettingsManager implements Serializable {

    public enum LogSpeed {
        SLOW, FAST, FASTER
    }

    private boolean autosave = true;
    private LogSpeed logSpeed = LogSpeed.FAST;
    private LogSpeed combatLogSpeed = LogSpeed.FAST;

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
}
