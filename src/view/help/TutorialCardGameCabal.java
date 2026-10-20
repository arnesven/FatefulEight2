package view.help;

import view.GameView;

public class TutorialCardGameCabal extends SubChapterHelpDialog {
    private static final String[] TEXT = new String[]{
            "Cabal is a card game where a group of 'Friends' are cooperating to try to " +
            "pick the Star cards from their hands of cards. However, some of the players are actually " +
            "Traitors and are secretly working against the Friends. At the start of the game, each player " +
            "draws a stone from a bag. If you draw a white stone you are a Friend, if you draw one of " +
            "the black stones you are a Traitor.\n\n" +
            "The objective of the Friends is to draw out all of the Star cards in the deck, before the end of the game. " +
            "If they fail, the Traitors win. The deck also has two Dagger cards. If both Daggers are drawn, " +
            "the Traitors immediately win. The rest of the cards in the deck are Junk cards, and have no effect.\n\n" +
            " Players  Traitors  Stars Cards\n" +
            "    5         2          6\n" +
            "    6         2          7\n" +
            "    7         3          7\n\n" +
            "The game is played over four turns. At the start of each turn, all cards are dealt out to the players. " +
            "Five cards are dealt to each player in the first turn, four in the next, " +
            "three on the third turn and two cards each on the " +
            "last turn.",

            "Play proceeds by player picking from each others' hands until a number of cards have been " +
            "picked equal to the number of players. A player who gets picked from gets to take the next pick.\n\n" +
            "At the end of a turn, cards which remain in players' hands are returned and shuffled into a new deck.\n\n"+
            "At the start of each turn, each player has the chance to make a Claim, to notify other players " +
            "about their hand. Players may make any claim, even untruthful ones! At the end of each turn, each player " +
            "gets the opportunity to update their claim. Players should try to deduce who is a Friend and who is " +
            "a Traitor by carefully considering the claims.\n\n" +
            "Whichever team wins the game will split the pot which was anted at the start of the game."};

    public TutorialCardGameCabal(GameView view) {
        super(view, "Cabal", TEXT);
    }
}
