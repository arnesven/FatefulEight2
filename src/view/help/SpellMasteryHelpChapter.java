package view.help;

import view.GameView;

public class SpellMasteryHelpChapter extends SubChapterHelpDialog {
    private static final String TEXT = "Some spells support Masteries. " +
            "When you cast such a spell a certain number of times, you will get a level of mastery in that spell, " +
            "boosting its effect each time you successfully cast it.";

    public SpellMasteryHelpChapter(GameView view, String str) {
        super(view, "Spell Masteries", str + "\n\n" + TEXT);
    }
}
