package model.states.cardgames;

import model.Model;
import model.SteppingMatrix;
import model.races.Race;
import model.states.CardGameState;
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
    public void setup(CardGameState state) {
        SteppingMatrix<CardGameObject> matrix = getMatrix();
        matrix.clear();
        addDeckToPlayArea();
        super.dealCardsToPlayers(state, deck, 6);
        state.println("The deck is shuffled and 6 cards are dealt to each player. ");
        discardPile.add(deck.remove(0));
        matrix.addElement(matrix.getColumns()/2, matrix.getRows()/2-1, discardPile);
        this.startingPlayer = MyRandom.sample(getPlayers());
        foldedPlayers = new HashSet<>();
        state.println("Each player antes 1 obol.");
        for (CardGamePlayer p : getPlayers()) {
            state.addHandAnimation(p, false, false, true);
            p.addToBet(1);
        }
        state.println(startingPlayer.getName() + " will start the game. Press enter to continue.");
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
        RaiseCardGameObject raise = new RaiseCardGameObject();
        raise.doAction(model, state, this, currentPlayer);
        deck.doAction(model, state, this, currentPlayer);
        currentPlayer.getCard(0).doAction(model, state, this, currentPlayer);
    }

    private void takePlayerTurn(Model model, CardGameState state, CardGamePlayer currentPlayer) {
        state.println("It's your turn. ");
        drawFromDeckOrDiscard(model, state, currentPlayer);
        discardFromHand(model, state, currentPlayer);
        raiseOrPass(model, state, currentPlayer);
    }

    private void drawFromDeckOrDiscard(Model model, CardGameState state, CardGamePlayer currentPlayer) {
        CardGameObject deckOrDiscard = null;
        setCursorEnabled(true);
        do {
            state.print("Draw a card from the deck or from the discard pile, or raise the bet.");
            state.waitForReturn();
            deckOrDiscard = getMatrix().getSelectedElement();
        } while (deckOrDiscard != deck && deckOrDiscard != discardPile);
        setCursorEnabled(false);
        deckOrDiscard.doAction(model, state, this, currentPlayer);

    }

    private void discardFromHand(Model model, CardGameState state, CardGamePlayer currentPlayer) {
        CardGameObject cardToDiscard = null;
        setCursorEnabled(true);
        do {
            state.print("Select a card from your hand to discard.");
            state.waitForReturn();
            cardToDiscard = getMatrix().getSelectedElement();
        } while (!(cardToDiscard instanceof CardGameCard) || !currentPlayer.hasCardInHand((CardGameCard) cardToDiscard));
        setCursorEnabled(false);
        cardToDiscard.doAction(model, state, this, currentPlayer);
    }

    private void raiseOrPass(Model model, CardGameState state, CardGamePlayer currentPlayer) {
        RaiseCardGameObject raise = new RaiseCardGameObject();
        getMatrix().addElement(4, getMatrix().getRows()-2, raise);
        ButtonCardGameObject pass = new PassCardGameObject();
        getMatrix().addElement(6, getMatrix().getRows()-2, pass);
        CardGameObject button = null;
        getMatrix().setSelectedPoint(pass);
        setCursorEnabled(true);
        do {
            state.print("Would you like to raise the bet, or pass?");
            state.waitForReturn();
            button = getMatrix().getSelectedElement();
        } while (button != raise && button != pass);
        setCursorEnabled(false);
        button.doAction(model, state, this, currentPlayer);

        getMatrix().remove(raise);
        getMatrix().remove(pass);
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
        state.addHandAnimation(currentPlayer, true, false, false);
        discardPile.add(cardGameCard);
        state.waitForAnimationToFinish();
    }

    @Override
    public void doOtherCardAction(Model model, CardGameState state, CardGamePlayer currentPlayer, CardGameCard cardGameCard) {
        // Unused for Runny
    }
}
