package view.help;

import view.GameView;

public class TutorialQuestOffers extends SubChapterHelpDialog {
    private static final String TEXT =
            "Each evening you spend at a town " +
            "or castle, you will be offered a number of random quest. If you accept, the party will " +
            "set out on the quest the following day.\n\n" +
            "In the Quest Offer view you can see a summary of the what the quest entails, as well as what " +
            "rewards you can expect if you complete the quest.\n\n" +
            "Some quests will be marked with an Move text, e.g. 'Move 2'. This means that " +
            "the quest takes place in a remote location and the party will move up to 2 hexes before starting the quest.\n\n" +
            "Some quest can be Held. A held quest will continue to be offered as long as you are in that " +
            "town or castle.\n\n" +
            "Once you have successfully completed a quest in a town or castle, you will be offered much " +
            "fewer quests in that location.";

    public TutorialQuestOffers(GameView view) {
        super(view, "Quest Offers", TEXT);
        setLevel(1);
    }
}
