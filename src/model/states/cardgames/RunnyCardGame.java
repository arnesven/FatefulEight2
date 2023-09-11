package model.states.cardgames;

import model.SteppingMatrix;
import model.races.Race;

import java.util.List;

public class RunnyCardGame extends CardGame {

    public static final int MAX_NUMBER_OF_PLAYERS = 4;
    private final CardGameDeck deck;
    private CardPile discard = new CardPile();

    public RunnyCardGame(List<Race> npcRaces) {
        super("Runny", npcRaces);
        this.deck = new CardGameDeck();
        addDeckToPlayArea();
    }

    @Override
    public void setup() {
        SteppingMatrix<CardGameObject> matrix = getMatrix();
        matrix.clear();
        addDeckToPlayArea();
        super.dealCardsToPlayers(deck, 6);
        discard.add(deck.remove(0));
        matrix.addElement(matrix.getColumns()/2, matrix.getRows()/2-1, discard);
    }

    private void addDeckToPlayArea() {
        SteppingMatrix<CardGameObject> matrix = getMatrix();
        matrix.addElement(matrix.getColumns()/2-1, matrix.getRows()/2-1, deck);
    }
}
