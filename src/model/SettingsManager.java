package model;

public class SettingsManager {

    public enum LogSpeed {
        SLOW, FAST, FASTER
    }

    private static boolean autosave = true;
    private static LogSpeed logSpeed = LogSpeed.FAST;

    public static void toggleAutosave() {
        autosave = !autosave;
    }

    public static boolean autosaveEnabled() {
        return autosave;
    }

    public static LogSpeed getLogSpeed() {
        return logSpeed;
    }

    public static String logSpeedAsText() {
        switch (logSpeed) {
            case FAST:
                return "FAST";
            case FASTER:
                return "FASTER";
            default:
                return "SLOW";
        }
    }

    public static void toggleLogSpeed() {
        if (logSpeed == LogSpeed.FAST) {
            logSpeed = LogSpeed.FASTER;
        } else if (logSpeed == LogSpeed.SLOW) {
            logSpeed = LogSpeed.FAST;
        } else {
            logSpeed = LogSpeed.SLOW;
        }
    }
}
