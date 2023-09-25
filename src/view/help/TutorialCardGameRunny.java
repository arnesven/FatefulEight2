package view.help;

import view.GameView;

public class TutorialCardGameRunny extends HelpDialog {
    private static final String TEXT =
            "Runny is played with a deck of 60 cards. Cards are valued from 0 to 9, in three suits, " +
            "and two copies of each card. Players are dealt 6 cards at the start of the game and try to " +
            "'lock' all their cards in either runs (consecutive valued cards of the same suit) of length 3, " +
            "or sets (cards of the same value) of three.\n\n" +
            "Players ante 1 obol at the start of the game and have the option to raise the bet each " +
            "turn up to a maximum bid. The maximum bid is also the minimum required amount of obols required to join the game." +
            "A raised bet will require the following players to either call the bet or fold their hand.\n\n" +
            "During their turn the player has the option of either drawing a card from the deck, or " +
            "from the discard pile. Then, the player must discard one card from their hand. If all cards " +
            "are 'locked' when a player discard their card, that player wins. A player also wins if all other " +
            "players declines to call the players bet.\n\n" +
            "The winner of each round collects the entire pot of obols.";

    public TutorialCardGameRunny(GameView view) {
        super(view, "Runny (Card Game)", TEXT);
    }
}
