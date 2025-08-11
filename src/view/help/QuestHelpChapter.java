package view.help;

import view.GameView;

import java.util.List;

public class QuestHelpChapter extends ExpandableHelpDialog {
    private static final String TEXT =
            "Quests can " +
            "generally be found in towns and castles. Quests are often also a good " +
            "way of earning some gold and experience points for your party. " +
                    "Most quest also unlock achievements.";

    public QuestHelpChapter(GameView view) {
        super(view, "Quests", TEXT, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        return List.of(new TutorialQuestOffers(view),
                       new TutorialGoingOnQuests(view));
    }
}
