package model.states.cardgames;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CardGameDeck extends ArrayList<CardGameCard> {
    private static final MyColors[] CARD_SUITS = new MyColors[]{MyColors.GREEN, MyColors.BLUE, MyColors.RED};
    private static final int MAX_VALUE = 12;
    private static Map<MyColors, Sprite[]> sprites = makeSpriteMap();

    public CardGameDeck() {
        for (MyColors suit : CARD_SUITS) {
            for (int i = 0; i < MAX_VALUE + 1; ++i) {
                this.add(new CardGameCard(i, suit));
            }
        }
        Collections.shuffle(this);
    }

    public static Sprite getSpriteForCard(CardGameCard cardGameCard) {
        return sprites.get(cardGameCard.getSuit())[cardGameCard.getValue()];
    }

    public CardGameCard drawCard() {
        return remove(0);
    }


    private static Map<MyColors, Sprite[]> makeSpriteMap() {
        Map<MyColors, Sprite[]> result = new HashMap<>();
        for (MyColors suit : CARD_SUITS) {
            Sprite[] sprites = new Sprite[MAX_VALUE+1];
            for (int i = 0; i < MAX_VALUE+1; ++i) {
                sprites[i] = new Sprite16x16("cardgamecard"+suit.name()+i, "cardgame.png", i,
                        MyColors.BLACK, MyColors.WHITE, suit, MyColors.BEIGE);
            }
            result.put(suit, sprites);
        }
        return result;
    }
}
