package model.states.cardgames;

import model.Model;
import model.SteppingMatrix;
import model.races.Race;
import model.states.CardGameState;
import model.states.GameState;
import util.Arithmetics;
import util.MyRandom;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RunnyCardGame extends CardGame {

    public static final int MAX_NUMBER_OF_PLAYERS = 4;
    private final CardGameDeck deck;
    private CardPile discardPile = new CardPile();
    private CardGamePlayer startingPlayer;
    private boolean winDeclared = false;
    private Set<CardGamePlayer> foldedPlayers;

    public RunnyCardGame(List<Race> npcRaces) {
        super("Runny", npcRaces);
        this.deck = new CardGameDeck();
        addDeckToPlayArea();
    }

    @Override
    public void setup(GameState state) {
        SteppingMatrix<CardGameObject> matrix = getMatrix();
        matrix.clear();
        addDeckToPlayArea();
        super.dealCardsToPlayers(deck, 6);
        discardPile.add(deck.remove(0));
        matrix.addElement(matrix.getColumns()/2, matrix.getRows()/2-1, discardPile);
        this.startingPlayer = MyRandom.sample(getPlayers());
        foldedPlayers = new HashSet<>();
        state.println("The deck is shuffled and 6 cards are dealt to each player. " + startingPlayer.getName() +
                " will start the game. Press enter to continue.");
    }

    @Override
    public void playRound(Model model, CardGameState state) {
        int playerIndex = getPlayers().indexOf(startingPlayer);
        while (!gameOver()) {
            CardGamePlayer currentPlayer = getPlayers().get(playerIndex);
            if (currentPlayer.isNPC()) {
                takeNPCTurn(model, state, currentPlayer);
            } else {
                takePlayerTurn(model, state, currentPlayer);
            }
            playerIndex = Arithmetics.incrementWithWrap(playerIndex, getPlayers().size());
        }
    }

    private void takeNPCTurn(Model model, CardGameState state, CardGamePlayer currentPlayer) {
        state.print(currentPlayer.getName() + "'s turn. ");
        deck.doAction(model, state, this, currentPlayer);
        currentPlayer.getCard(0).doAction(model, state, this, currentPlayer);
    }

    private void takePlayerTurn(Model model, CardGameState state, CardGamePlayer currentPlayer) {
        CardGameObject deckOrDiscard = null;
        do {
            state.print("It's your turn. Draw a card from the deck or from the discard pile.");
            setCursorEnabled(true);
            state.waitForReturn();
            deckOrDiscard = getMatrix().getSelectedElement();
        } while (deckOrDiscard != deck && deckOrDiscard != discardPile);
        setCursorEnabled(false);
        deckOrDiscard.doAction(model, state, this, currentPlayer);

        CardGameObject cardToDiscard = null;
        do {
            state.print("Select a card from your hand to discard.");
            setCursorEnabled(true);
            state.waitForReturn();
            cardToDiscard = getMatrix().getSelectedElement();
        } while (!(cardToDiscard instanceof CardGameCard) || !currentPlayer.hasCardInHand((CardGameCard) cardToDiscard));
        cardToDiscard.doAction(model, state, this, currentPlayer);
        setCursorEnabled(false);
    }

    private boolean gameOver() {
        return foldedPlayers.size() == getPlayers().size()-1 || winDeclared;
    }

    private void addDeckToPlayArea() {
        SteppingMatrix<CardGameObject> matrix = getMatrix();
        matrix.addElement(matrix.getColumns()/2-1, matrix.getRows()/2-1, deck);
    }

    @Override
    public void doCardInHandAction(Model model, CardGameState state, CardGamePlayer currentPlayer, CardGameCard cardGameCard) {
        state.println((currentPlayer.isNPC() ? currentPlayer.getName() : "You") + " discards " + cardGameCard.getText() + ".");
        currentPlayer.removeCard(cardGameCard, this);
        state.addHandAnimation(currentPlayer);
        discardPile.add(cardGameCard);
    }

    @Override
    public void doOtherCardAction(Model model, CardGameState state, CardGamePlayer currentPlayer, CardGameCard cardGameCard) {
        // Unused for Runny
    }
}
