package view.help;

import view.GameView;

public class TutorialSneakAttack extends SubChapterHelpDialog {
    private static final String TEXT =
            "A character with at least 1 rank of sneaking can perform a sneak " +
            "attack once per combat. Unless that character has already attacked, " +
            "or been attacked the character can initiate the sneak attack as a " +
            "combat action.\n\n" +
            "The character rolls a D10 and adds his sneaking modifier " +
            "to get a Stealth rating. The rest of the sneak attack will be resolved at " +
            "the end of the combat round. Should the character be targeted by an " +
            "attack in the mean time a D10 is rolled. If the result + 5 is below " +
            "the Stealth rating another eligible target is chosen instead (normally " +
            "from the front row), otherwise the character is hit by the attack and " +
            "the sneak attack is cancelled (even if the character evades, blocks or " +
            "the attack does 0 damage).\n\n" +
            "Upon resolution, the sneak attack works like a " +
            "normal attack, albeit with 3 times the damage. This damage multiplier can " +
            "stack with critical hits for a total of x6.";

    public TutorialSneakAttack(GameView view) {
        super(view, "Sneak Attack", TEXT);
        setLevel(2);
    }
}
