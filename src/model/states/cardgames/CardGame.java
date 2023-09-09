package model.states.cardgames;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.races.Race;
import model.states.GameState;
import util.MyRandom;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CardGame {
    private final List<CardGamePlayer> players;
    private CardGamePlayer characterPlayer;
    private SteppingMatrix<CardGameCard> playerHand = new SteppingMatrix<>(10, 1);

    public CardGame(List<Race> npcRaces) {
        this.players = new ArrayList<>();
        for (Race r : npcRaces) {
            boolean gender = MyRandom.flipCoin();
            players.add(new CardGamePlayer(GameState.randomFirstName(gender), gender, r, MyRandom.randInt(20, 40)));
        }
    }

    public abstract void setup();

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
        this.characterPlayer = new CardGamePlayer(leader.getFirstName(), leader.getGender(), leader.getRace(), obols);
        players.add(characterPlayer);
    }

    protected void dealCardsToPlayers(CardGameDeck deck, int amount) {
        for (CardGamePlayer p : players) {
            for (int i = 0; i < amount; ++i) {
                p.giveCard(deck.drawCard());
            }
            Collections.sort(p.getCards());
        }
        playerHand.clear();
        playerHand.addElements(characterPlayer.getCards());
    }

    public CardGamePlayer getCharacterPlayer() {
        return characterPlayer;
    }

    public boolean cursorEnabled() {
        return true;
    }

    public CardGameCard getSelectedCard() {
        return playerHand.getSelectedElement();
    }

    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        return playerHand.handleKeyEvent(keyEvent);
    }
}
