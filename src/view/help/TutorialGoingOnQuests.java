package view.help;

import view.GameView;

public class TutorialGoingOnQuests extends SubChapterHelpDialog {
    private static final String text =
            "During quests, the leader will make decisions about which paths to take. At " +
            "decision points, the leader must pass a Leadership 6 check. If the check fails " +
            "one of the options is chosen for the party instead.\n\n" +
            "Quest can either be successful or failed. A quest is successful if the party can " +
            "successfully perform all the tasks required to reach the green star node. A quest " +
            "is failed if the party reaches the red X node.\n\n" +
            "You cannot always flee from combat during quests, so be very careful.\n\n" +
            "When a quest is successfully completed rewards usually include increased " +
            "party reputation, gold and or experience points for the party members.";

    public TutorialGoingOnQuests(GameView view) {
        super(view, "Going on Quests", text);
        setLevel(1);
    }
}
