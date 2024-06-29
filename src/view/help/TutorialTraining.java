package view.help;

import view.GameView;
import view.help.HelpDialog;

public class TutorialTraining extends HelpDialog {
    private static final String TEXT =
            "Training can be purchased at temples for 5 gold and is a good " +
            "way to actively increase the experience points of individual party members.\n\n" +
            "Each day, the temple offers six different training sessions. Three sessions " +
            "are related to magic and three will be related to martial skills. " +
            "Furthermore each session is at Novice, Advanced or Expert level.\n\n" +
            "    LEVEL     DIFF    EXP\n" +
            "    Novice       6     30\n" +
            "    Advanced     8     40\n" +
            "    Expert      10     50\n\n" +
            "The training session entails a skill check with a difficulty dependent " +
            "on the session level. If successful the participants gains the experience listed " +
            "in the table above.\n\n" +
            "You can assign at most three party members to a single training session. " +
            "You may also assign party members to 'Temple Chores', which give no " +
            "experience but at least pays a small salary of 1 gold.";

    public TutorialTraining(GameView view) {
        super(view, "Training", TEXT);
    }
}
