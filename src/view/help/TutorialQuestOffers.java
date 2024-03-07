package view.help;

import view.GameView;

public class TutorialQuestOffers extends SubChapterHelpDialog {
    private static final String TEXT =
            "Each evening you spend at a town " +
            "or castle, you will be offered a number of random quest. If you accept, the party will " +
            "set out on the quest the following day. Only one quest can be accepted per " +
            "town or castle.\n\n" +
            "Some quest can be Held. A held quest will continue to be offered as long as you are in that " +
            "town or castle.";

    public TutorialQuestOffers(GameView view) {
        super(view, "Quest Offers", TEXT);
        setLevel(1);
    }
}
