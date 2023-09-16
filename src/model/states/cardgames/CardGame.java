package model.states.cardgames;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.races.Race;
import model.states.CardGameState;
import model.states.GameState;
import util.MyRandom;
import view.MyColors;

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

    public CardGame(String name, List<Race> npcRaces) {
        this.name = name;
        this.players = new ArrayList<>();
        for (Race r : npcRaces) {
            boolean gender = MyRandom.flipCoin();
            players.add(new CardGamePlayer(GameState.randomFirstName(gender), gender, r, MyRandom.randInt(20, 40), true));
        }
    }

    public abstract void setup(CardGameState state);

    public abstract void playRound(Model model, CardGameState state);

    public CardGamePlayer getNPC(int i) {
        return players.get(i);
    }

    public int getNumberOfNPCs() {
        if (players.contains(characterPlayer)) {
            return players.size() - 1;
        }
        return players.size();
    }

    public void addPlayer(GameCharacter leader, int obols) {
        this.characterPlayer = new CardGamePlayer(leader.getFirstName(), leader.getGender(), leader.getRace(), obols, false);
        players.add(characterPlayer);
    }

    public List<CardGamePlayer> getPlayers() {
        return players;
    }

    protected void dealCardsToPlayers(CardGameState state, CardGameDeck deck, int amount) {
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

    protected void setCursorEnabled(boolean b) {
        cursorEnabled = b;
    }

    public abstract void doCardInHandAction(Model model, CardGameState state, CardGamePlayer currentPlayer, CardGameCard cardGameCard);

    public abstract void doOtherCardAction(Model model, CardGameState state, CardGamePlayer currentPlayer, CardGameCard cardGameCard);

    public void refreshPlayerHand(CardGamePlayer cardGamePlayer) {
        for (int i = 0; i < cardGamePlayer.numberOfCardsInHand(); ++i) {
            if (cardArea.getElementList().contains(cardGamePlayer.getCard(i))) {
                cardArea.remove(cardGamePlayer.getCard(i));
            }
        }
        int offset = (cardArea.getColumns() - characterPlayer.numberOfCardsInHand()) / 2;
        for (int i = 0; i < characterPlayer.numberOfCardsInHand(); ++i) {
            cardArea.addElement(offset + i, cardArea.getRows()-1, characterPlayer.getCard(i));
        }
    }
}
