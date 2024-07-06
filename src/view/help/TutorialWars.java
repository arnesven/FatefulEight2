package view.help;

import view.GameView;

public class TutorialWars extends HelpDialog {
    private static final String TEXT =
            "Sometimes kingdoms go to war. If you visit a castle of a kingdom which is at war " +
            "you may be offered to be recruited.\n\n" +
            "Wars are a series of battles fought between two kingdoms. During each battle you will " +
            "be offered to direct the units (see Battles) or fight alongside them.\n\n" +
            "After winning a battle you will get the chance to loot the battlefield and " +
            "reinforce the army you are fighting for.\n\n" +
            "If you win three battles against the other kingdom (before you lose three) the kingdom " +
            "you are fighting for will win the war. This will increase your reputation and experience points.";

    public TutorialWars(GameView view) {
        super(view, "Wars", TEXT);
    }
}
