package model.states.cardgames;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

public class CardGameCard implements CardGameObject, Comparable<CardGameCard> {
    private int number;
    private MyColors color;
    private boolean isFlipped = false;
    private static final Sprite FLIPPED = new Sprite16x16("cardgamedeck", "cardgame.png", 0x11,
            MyColors.BLACK, MyColors.BROWN, MyColors.PINK, MyColors.BEIGE);

    public CardGameCard(int i, MyColors suit) {
        this.number = i;
        this.color = suit;
    }

    @Override
    public int compareTo(CardGameCard other) {
        return number - other.number;
    }

    public Sprite getSprite() {
       if (isFlipped) {
           return FLIPPED;
       }
       return CardGameDeck.getSpriteForCard(this);
    }

    public void flipCard() {
        isFlipped = !isFlipped;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    @Override
    public String getText() {
        if (isFlipped) {
            return "A face-down card.";
        }
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
