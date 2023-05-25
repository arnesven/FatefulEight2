package view.help;

import view.GameView;

public class TutorialLeaderDialog extends HelpDialog {
    private static final String text =
            "Once you have recruited your first party member, you should start thinking about " +
            "who will be the party leader. The leader can announce retreats during combat, " +
            "will take decisions during quests and acts as a guide for the party in many situations.\n\n" +
            "Setting the leader can be done from the Party Menu. The leader cannot be changed " +
            "during combat, during quests or in dungeons.\n\n" +
            "It is recommended that the character with the highest leadership leads the party.\n\n" +
            "If your leader dies in combat, the character with the highest leadership will " +
            "automatically become the new leader.";

    public TutorialLeaderDialog(GameView view) {
        super(view, "Leader", text);
    }
}
