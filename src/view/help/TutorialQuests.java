package view.help;

import view.GameView;

public class TutorialQuests extends HelpDialog {
    private static final String text =
            "Quests are encountered in towns and castles. Quests are the main way of getting " +
            "Party Reputation. Each evening you spend at a town " +
            "or castle, you will be offered a random quest. If you accept, the party will " +
            "set on out on the quest the following day. Only one quest can be accepted per " +
            "town or castle.\n\n" +
            "During quests, the leader will make decisions about which paths to take. At " +
            "decision points, the leader must pass a Leadership 6 check. If the check fails " +
            "one of the options is chosen for the party instead.\n\n" +
            "Quest can either be successful or failed. A quest is successful if the party can " +
            "successfully perform all the task required to reach the green star node. A quest " +
            "is failed if the party reaches the red X node\n\n" +
            "Normally, you cannot flee from combat during quests, so be very careful.\n\n" +
            "When a quest is successfully completed rewards usually include increased " +
            "party reputation and gold per party member.";

    public TutorialQuests(GameView view) {
        super(view, "Quests", text);
    }
}
