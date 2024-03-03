package view.help;

import view.GameView;

public class TutorialEnemyAttacks1 extends SubChapterHelpDialog {
    private static final String TEXT =
            "Most enemies have regular attacks, but some have special attacks.\n\n" +
            "Magic attacks completely ignore armor " +
            "and cannot be blocked by shields. They can however be evaded.\n\n" +
            "Ranged attacks can attack targets both in the front and back row.\n\n" +
            "Poison attacks can cause the Poison condition, which has no additional effect " +
            "in combat, but causes the character to suffer 2 damage each day until that character " +
            "can recover at an inn or tavern, or consumes an antidote potion.\n\n" +
            "Paralysis attacks can cause a character to be paralysed, which prevents the character " +
            "from acting in combat. Paralysis can be cured by Anti-Paralysis potions.\n\n" +
            "Multi attacks can target more than one target in a single attack.\n\n" +
            "Bleeding attacks can cause a character to become bleeding, which causes the character to suffer 1 damage " +
            "per combat round until healed in any way.";

    public TutorialEnemyAttacks1(GameView view) {
        super(view, "Enemy Attacks I", TEXT);
    }
}
