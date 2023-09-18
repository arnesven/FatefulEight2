package model.states.cardgames;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CardGameDeck extends ArrayList<CardGameCard> implements CardGameObject {
    public static final MyColors[] CARD_SUITS = new MyColors[]{MyColors.GREEN, MyColors.BLUE, MyColors.RED};
    public static final int MAX_VALUE = 9;
    private static final Sprite SPRITE = new Sprite16x16("cardgamedeck", "cardgame.png", 0x15,
            MyColors.BLACK, MyColors.BROWN, MyColors.PINK, MyColors.BEIGE);
    private static Map<MyColors, Sprite[]> sprites = makeSpriteMap();

    public CardGameDeck() {
        for (int j = 0; j < 2; ++j) {
            for (MyColors suit : CARD_SUITS) {
                for (int i = 0; i < MAX_VALUE + 1; ++i) {
                    this.add(new CardGameCard(i, suit));
                }
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

    @Override
    public Sprite getSprite() {
        if (isEmpty()) {
            return BLANK_CARD_SPRITE;
        }
        return SPRITE;
    }

    @Override
    public String getText() {
        return "The Deck";
    }

    @Override
    public void doAction(Model model, CardGameState state, CardGame cardGame, CardGamePlayer currentPlayer) {
        if (isEmpty()) {
            state.println("Deck is empty. The discard is reshuffled.");
            cardGame.reshuffleDeck();
        }
        CardGameCard card = drawCard();
        state.addHandAnimation(currentPlayer, false, true, false);
        if (currentPlayer.isNPC()) {
            state.println(currentPlayer.getName() + " draws a card from the deck.");
        } else {
            state.println("You draw a " + card.getText() + " from the deck.");
        }
        state.waitForAnimationToFinish();
        currentPlayer.giveCard(card, cardGame);
    }

    @Override
    public boolean hasSpecialCursor() {
        return false;
    }

    @Override
    public Sprite getCursorSprite() {
        return null;
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
