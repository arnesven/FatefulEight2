package model.states.cardgames;

import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.util.ArrayList;

public class CardPile extends ArrayList<CardGameCard> implements CardGameObject {

    private static final Sprite BLANK = new Sprite16x16("blankcard", "cardgame.png", 0x21);

    public CardGameCard topCard() {
        return get(size()-1);
    }

    @Override
    public Sprite getSprite() {
        if (isEmpty()) {
            return BLANK;
        }
        return topCard().getSprite();
    }

    @Override
    public String getText() {
        if (isEmpty()) {
            return "Discard (empty)";
        }
        return "Discard: " + topCard().getText();
    }
}
