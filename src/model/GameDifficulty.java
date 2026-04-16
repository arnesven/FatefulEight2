package model;

public enum GameDifficulty {
    EASY, NORMAL, HARD, IMPOSSIBLE;

    public String getDescription() {
        return switch (this) {
            case EASY -> "Skill checks and combat events are slightly easier. Negative party events less frequent.";
            case NORMAL -> "No adjustment.";
            case HARD -> "Skill checks and combat events are slightly harder.";
            case IMPOSSIBLE -> "Skill checks and combat events are much harder. Autosaving disabled.";
        };
    }
}
