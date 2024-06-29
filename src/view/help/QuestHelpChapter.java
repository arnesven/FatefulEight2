package view.help;

import view.GameView;

import java.util.List;

public class QuestHelpChapter extends ExpandableHelpDialog {
    private static final String TEXT =
            "Quests are the main qay of getting Reputation points and can " +
            "generally be found in towns and castles. Quests are often also a good " +
            "way of earning some gold and experience points for your party.";

    public QuestHelpChapter(GameView view) {
        super(view, "Quests", TEXT, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        return List.of(new TutorialQuestOffers(view),
                       new TutorialGoingOnQuests(view));
    }
}
