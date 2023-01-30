package view.help;

import view.GameView;

public class TutorialClassesDialog extends HelpDialog {
    private static final String text =
            "A character's class defines that characters baseline of Health Points and Speed, " +
            "their skills and whether or not that character can wear heavy armor. A character's " +
            "class also affects how much gold they contribute to the party when joining and " +
            "how much gold they would claim if being dismissed.\n\n" +
            "Each character has four classes which he or she may assume. Various events will allow " +
            "characters to change their class. Think carefully before switching classes, you may not " +
            "get a chance to switch back soon!";

    public TutorialClassesDialog(GameView view) {
        super(view, "Classes", text);
    }
}
