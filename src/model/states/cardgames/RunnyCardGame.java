package model.states.cardgames;

import model.races.Race;

import java.util.List;

public class RunnyCardGame extends CardGame {

    private final CardGameDeck deck;

    public RunnyCardGame(List<Race> npcRaces) {
        super(npcRaces);
        this.deck = new CardGameDeck();
    }

    @Override
    public void setup() {
        super.dealCardsToPlayers(deck, 6);
    }
}
