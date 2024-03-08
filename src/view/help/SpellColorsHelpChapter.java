package view.help;

import view.GameView;

public class SpellColorsHelpChapter extends SubChapterHelpDialog {
    private static final String TEXT =
            "Red spells are focused on destruction and combat.\n\n" +
            "Green spells are focused on nature.\n\n" +
            "White spells are focused on healing, light and holy power.\n\n" +
            "Black spells revolve around death, darkness and decay.\n\n" +
            "Blue spells are usually focused on illusion and altering reality or state of mind.\n\n" +
            "Colorless spells usually deal with meta-magic. Colorless spells uses the " +
            "Magic (any) Skill when cast.";

    public SpellColorsHelpChapter(GameView view) {
        super(view, "Spell Colors", TEXT);
    }
}
