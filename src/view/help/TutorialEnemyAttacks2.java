package view.help;

import view.GameView;

public class TutorialEnemyAttacks2 extends SubChapterHelpDialog {
    private static final String TEXT =
            "Most enemies have regular attacks, but some have special attacks.\n\n" +
            "Knock Back attacks can move the target from the front row to the back row.\n\n" +
            "Pull Forward attacks can move the target from the back row to the front row.\n\n" +
            "Knock Down attacks can paralyze the target for one turn.\n\n" +
            "Ferocious attacks are simply attacks of high damage - watch out!";

    public TutorialEnemyAttacks2(GameView view) {
        super(view, "Enemy Attacks II", TEXT);
    }
}
