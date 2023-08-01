package view.help;

import view.GameView;

public class TutorialCombatResting extends SubChapterHelpDialog {
    private static final String TEXT =
            "Beginning at level 3, a character who is in the back row in combat " +
            "can perform the Rest ability. When resting the character recovers " +
            "either health or stamina.\n\n" +
            "A character at full health recovers 1 Stamina Point. " +
            "A character at full stamina recovers 1 Health Point. " +
            "A character neither at full health nor full stamina recovers " +
            "either 1 Health Point or 1 Stamina at equal probability.";

    public TutorialCombatResting(GameView view) {
        super(view, "Resting", TEXT);
        setLevel(2);
    }
}
