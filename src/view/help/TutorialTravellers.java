package view.help;

import view.GameView;

public class TutorialTravellers extends HelpDialog {
    private static final String TEXT = "Travellers can be found in different locations in the world, but often " +
            "at inns and taverns, and will be looking for an escort.\n\n" +
            "Should you accept, and successfully take the traveller to the destination within a given number of days, you will be rewarded. " +
            "However, if you miss the deadline, the reward will be halved. " +
            "If you miss the deadline by 5 days the traveller will abandon you." +
            "\n\nA traveller may refuse to be escorted if you have too few party members " +
            "in your party and your level is too low or if you are too notorious.\n\n" +
            "Travellers are not part of your party and thus do not participate in combat, quests or events which happen to you. " +
            "They have their own rations and can always keep up with you, even when you are riding or travelling by sea.\n\n" +
            "You can escort at most two travellers at a time.";

    public TutorialTravellers(GameView view) {
        super(view, "Travellers", TEXT);
    }
}
