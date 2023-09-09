package model.states.cardgames;

import view.MyColors;
import view.sprites.Sprite;

public class CardGameCard implements Comparable<CardGameCard> {
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

    public MyColors getSuit() {
        return color;
    }

    public int getValue() {
        return number;
    }
}
