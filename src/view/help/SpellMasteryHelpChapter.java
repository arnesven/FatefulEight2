package view.help;

import view.GameView;

public class SpellMasteryHelpChapter extends SubChapterHelpDialog {
    private static final String TEXT = "Some spells support Masteries. " +
            "When you cast such a spell a certain number of times, you will get a level of mastery in that spell, " +
            "boosting its effect each time you successfully cast it.\n\n" +
            "Each character's mastery of a spell, along with the progress toward next mastery level " +
            "can be seen from the Spell menu. Spells which support masteries are marked with an 'M' in the Spell menu.\n\n" +
            "Sometimes you will encounter magic users in the world who will offer to tutor the party in specific spells masteries.";

    public SpellMasteryHelpChapter(GameView view, String str) {
        super(view, "Spell Masteries", str + "\n\n" + TEXT);
    }
}
