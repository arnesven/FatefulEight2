package view.help;

import view.GameView;

public class TutorialCardGameKnockOut extends SubChapterHelpDialog {
    private static final String TEXT =
            "Knock-Out is a card game played with a deck of 24 cards. " +
            "Each player is dealt 1 card at the beginning of the game and draws 1 card at the beginning of each turn, then " +
            "chooses one of the two cards to play. The object of the game is to have the highest valued card when the deck " +
            "runs out, or to be the last player remaining. There are the following cards:\n\n" +
            "1 - CUDGEL (8): Guess another players card (but not Cudgel). If you're right, they get knocked out.\n" +
            "2 - SPYGLASS (3): Look at another players hand.\n" +
            "3 - RAPIER (3): Compare your card with another player's. The player with the lower card is knocked out.\n" +
            "4 - SHIELD (3): Protects you from being target by other cards until your next turn.\n" +
            "5 - CROSSBOW (3): Force another player to discard their card and draw card.\n" +
            "6 - MAGIC MIRROR (2): Switch cards with another player.\n" +
            "7 - FALSE SCEPTER (1): No effect when played, but you must play this card if you are holding a 5 or a 6.\n" +
            "8 - SCEPTER (1): If you play or discard this card, you are knocked out!";

    public TutorialCardGameKnockOut(GameView view) {
        super(view, "Knock-Out", TEXT);
    }
}
