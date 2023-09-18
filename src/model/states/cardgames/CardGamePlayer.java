package model.states.cardgames;

import model.Model;
import model.races.Race;
import model.states.cardgames.runny.RunnyCardGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CardGamePlayer {
    private final boolean isNPC;
    private String name;
    private boolean gender;
    private Race race;
    private int obols;
    private List<CardGameCard> cards = new ArrayList<>();
    private int currentBet = 0;

    public CardGamePlayer(String firstName, boolean gender, Race race, int obols, boolean isNPC) {
        this.name = firstName;
        this.gender = gender;
        this.race = race;
        this.obols = obols;
        this.isNPC = isNPC;
    }

    public abstract void takeTurn(Model model, CardGameState state, RunnyCardGame runnyCardGame);

    public Race getRace() {
        return race;
    }

    public String getName() {
        return name;
    }

    public void giveCard(CardGameCard card, CardGame cardGame) {
        cards.add(card);
        Collections.sort(cards);
        if (!isNPC) {
            cardGame.refreshPlayerHand(this);
        }
    }

    public void removeCard(CardGameCard card, CardGame cardGame) {
        cards.remove(card);
        if (!isNPC) {
            cardGame.getMatrix().remove(card);
            cardGame.refreshPlayerHand(this);
        }
    }

    public CardGameCard removeCard(int index, CardGame cardGame) {
        CardGameCard toReturn = cards.get(index);
        removeCard(toReturn, cardGame);
        return toReturn;
    }

    public int getObols() {
        return obols;
    }

    public int numberOfCardsInHand() {
        return cards.size();
    }

    public CardGameCard getCard(int i) {
        return cards.get(i);
    }

    public boolean hasCardInHand(CardGameCard cardGameCard) {
        return cards.contains(cardGameCard);
    }

    public boolean isNPC() {
        return isNPC;
    }

    public void addToBet(int i) {
        obols -= i;
        currentBet += i;
    }

    public int getBet() {
        return currentBet;
    }

    public void resetBet() {
        currentBet = 0;
    }

    public void addToObols(int winPot) {
        obols += winPot;
    }

    public void clearCards() {
        cards.clear();
    }
}
