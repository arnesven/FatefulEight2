package model.states.cardgames;

import model.Model;
import model.states.CardGameState;
import view.MyColors;
import view.sprites.Sprite;

public class CardGameCard implements CardGameObject, Comparable<CardGameCard> {
    private int number;
    private MyColors color;

    public CardGameCard(int i, MyColors suit) {
        this.number = i;
        this.color = suit;
    }

    @Override
    public int compareTo(CardGameCard other) {
        return number - other.number;
    }

    public Sprite getSprite() {
        return CardGameDeck.getSpriteForCard(this);
    }

    @Override
    public String getText() {
        return color.name() + " " + number;
    }

    @Override
    public void doAction(Model model, CardGameState state, CardGame cardGame, CardGamePlayer currentPlayer) {
        if (currentPlayer.hasCardInHand(this)) {
            cardGame.doCardInHandAction(model, state, currentPlayer, this);
        } else {
            cardGame.doOtherCardAction(model, state, currentPlayer, this);
        }
    }

    @Override
    public boolean hasSpecialCursor() {
        return false;
    }

    @Override
    public Sprite getCursorSprite() {
        return null;
    }

    public MyColors getSuit() {
        return color;
    }

    public int getValue() {
        return number;
    }

    @Override
    public String toString() {
        return "CardGameCard{" +
                "number=" + number +
                ", color=" + color +
                '}';
    }
}
