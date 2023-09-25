package model.states.cardgames;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CardGame {
    private final List<CardGamePlayer> players;
    private CardGamePlayer characterPlayer;
    private SteppingMatrix<CardGameObject> cardArea = new SteppingMatrix<>(14, 16);
    private final String name;
    private boolean cursorEnabled = false;
    private CardGameDeck deck;
    private CardPile discardPile = new CardPile();
    private int currentBet = 0;

    public CardGame(String name, List<CardGamePlayer> npcPlayers, CardGameDeck deck) {
        this.name = name;
        this.players = new ArrayList<>(npcPlayers);
        setDeck(deck);
        cardArea.addElement(cardArea.getColumns()/2, cardArea.getRows()/2-1, discardPile);
    }

    public abstract void setup(CardGameState state);

    public abstract void playRound(Model model, CardGameState state);

    public abstract void foldPlayer(Model model, CardGameState state, CardGamePlayer player);

    public CardGamePlayer getNPC(int i) {
        return players.get(i);
    }

    protected abstract CardGamePlayer makeCharacterPlayer(GameCharacter leader, int obols);

    public int getNumberOfNPCs() {
        if (players.contains(characterPlayer)) {
            return players.size() - 1;
        }
        return players.size();
    }

    public void addPlayer(GameCharacter leader, int obols) {
        characterPlayer = makeCharacterPlayer(leader, obols);
        players.add(characterPlayer);
    }

    public List<CardGamePlayer> getPlayers() {
        return players;
    }

    protected void dealCardsToPlayers(CardGameState state, int amount) {
        removeCardsFromArea(characterPlayer);
        for (CardGamePlayer p : players) {
            p.clearCards();
        }
        for (int i = 0; i < amount; ++i) {
            for (CardGamePlayer p : players) {
                state.addCardDealtAnimation(p);
                p.giveCard(deck.drawCard(), this);
            }
        }
        refreshPlayerHand(characterPlayer);
    }

    public CardGamePlayer getCharacterPlayer() {
        return characterPlayer;
    }

    public boolean cursorEnabled() {
        return cursorEnabled;
    }

    public CardGameObject getSelectedObject() {
        return cardArea.getSelectedElement();
    }

    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        return cardArea.handleKeyEvent(keyEvent);
    }

    public SteppingMatrix<CardGameObject> getMatrix() {
        return cardArea;
    }

    public String getName() {
        return this.name;
    }

    public String getUnderText(Model model) {
        if (getSelectedObject() == null) {
            return "";
        }
        if (cardArea.getSelectedPoint().y == cardArea.getRows()-1) {
            return "Your hand: " + cardArea.getSelectedElement().getText();
        }
        return cardArea.getSelectedElement().getText();
    }

    public void setCursorEnabled(boolean b) {
        cursorEnabled = b;
    }

    public abstract void doCardInHandAction(Model model, CardGameState state, CardGamePlayer currentPlayer, CardGameCard cardGameCard);

    public abstract void doOtherCardAction(Model model, CardGameState state, CardGamePlayer currentPlayer, CardGameCard cardGameCard);

    public void refreshPlayerHand(CardGamePlayer cardGamePlayer) {
        removeCardsFromArea(cardGamePlayer);
        int offset = (cardArea.getColumns() - characterPlayer.numberOfCardsInHand()) / 2;
        for (int i = 0; i < characterPlayer.numberOfCardsInHand(); ++i) {
            cardArea.addElement(offset + i, cardArea.getRows()-1, characterPlayer.getCard(i));
        }
    }

    public void removeCardsFromArea(CardGamePlayer cardGamePlayer) {
        for (int i = 0; i < cardGamePlayer.numberOfCardsInHand(); ++i) {
            if (cardArea.getElementList().contains(cardGamePlayer.getCard(i))) {
                cardArea.remove(cardGamePlayer.getCard(i));
            }
        }
    }

    public int getPlayerObols() {
        return characterPlayer.getObols();
    }

    public void reshuffleDeck() {
        deck.addAll(discardPile);
        discardPile.clear();
        Collections.shuffle(deck);
    }

    public CardGameDeck getDeck() {
        return deck;
    }

    public void setDeck(CardGameDeck deck) {
        if (cardArea.getElementList().contains(this.deck)) {
            cardArea.remove(this.deck);
        }
        this.deck = deck;
        cardArea.addElement(cardArea.getColumns()/2-1, cardArea.getRows()/2-1, deck);
    }

    public CardPile getDiscard() {
        return discardPile;
    }

    public void addToCurrentBet(int bet) {
        currentBet += bet;
    }

    public void resetCurrentBet() { currentBet = 0; }

    public int getCurrentBet() {
        return currentBet;
    }

    public ButtonCardGameObject twoButtonOption(CardGameState state,
                                                ButtonCardGameObject button1,
                                                ButtonCardGameObject button2,
                                                ButtonCardGameObject selected, String message) {
        getMatrix().addElement(4, getMatrix().getRows()-2, button1);
        getMatrix().addElement(6, getMatrix().getRows()-2, button2);
        CardGameObject button = null;
        getMatrix().setSelectedPoint(selected);
        setCursorEnabled(true);
        do {
            state.print(message);
            state.waitForReturn();
            button = getMatrix().getSelectedElement();
        } while (button != button1 && button != button2);
        setCursorEnabled(false);
        getMatrix().remove(button1);
        getMatrix().remove(button2);
        return (ButtonCardGameObject)button;
    }

    public int getMaximumBet() {
        return Integer.MAX_VALUE;
    }

    public abstract void replacePlayersLowOnObols(Model model, CardGameState cardGameState);
}
