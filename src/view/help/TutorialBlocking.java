package view.help;

import view.GameView;

public class TutorialBlocking extends SubChapterHelpDialog {
    private static final String TEXT =
            "In addition to providing Armor Points, shields also grant the bearer " +
            "a chance to block incoming attacks. Each shield has a block value. During " +
            "combat, when a character wielding a shield is attack a D10 is rolled. " +
            "If the result is less than or equal to the shields block value, the attack is blocked. " +
            "The attack still hits the character, but the shield absorbs this hit, and the character " +
            "takes no damage.";

    public TutorialBlocking(GameView view) {
        super(view, "Blocking", TEXT);
    }
}
