package view.help;

import view.GameView;

public class TutorialScrolls extends HelpDialog {
    private static final String TEXT =
            "Scrolls are one-time-use versions of their spell counterparts. They are consumed when you use them." +
            "A scroll can be cast in the same situations as the spell it is based on but has " +
            "the additional benefit of not costing any Health Points to cast.";

    public TutorialScrolls(GameView view) {
        super(view, "Scrolls", TEXT);
    }
}
