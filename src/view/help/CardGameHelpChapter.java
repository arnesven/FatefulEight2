package view.help;

import view.GameView;

import java.util.List;

public class CardGameHelpChapter extends ExpandableHelpDialog {
    private static final String TEXT = "Card games can be played at taverns and inns in the evening. " +
            "Card game use Obols for betting.";

    public CardGameHelpChapter(GameView view) {
        super(view, "Card Games", TEXT, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        return List.of(
                new TutorialCardGameKnockOut(view),
                new TutorialCardGameRunny(view));
    }
}
