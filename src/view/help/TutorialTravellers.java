package view.help;

import view.GameView;

public class TutorialTravellers extends HelpDialog {
    private static final String TEXT = "Travellers can be found in different locations in the world, but often " +
            "at inns and taverns. Often travellers will be looking for escorting parties and will pay you to deliver " +
            "them to a certain destination, within a given number of days.\n\n" +
            "Should you accept, and successfully take the traveller to the destination in time, you will be rewarded. " +
            "However, if you miss the deadline, the reward will be halved.\n\n" +
            "If you miss the deadline by 10 days the traveller will abandon you and attempt to reach the destination on their own." +
            "\n\nA traveller may refuse to be escorted if you have too few party members " +
            "in your party and your level is too low. A traveller may also refuse parties which are too notorious.\n\n" +
            "Travellers are not part of your party and thus do not participate in combat, quests or events which happen to you. " +
            "They have their own rations and can always keep up with you, even when you are riding.";

    public TutorialTravellers(GameView view) {
        super(view, "Travellers", TEXT);
    }
}
