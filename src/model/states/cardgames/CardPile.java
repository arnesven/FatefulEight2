package model.states.cardgames;

import model.Model;
import model.states.CardGameState;
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

    @Override
    public void doAction(Model model, CardGameState state, CardGame cardGame, CardGamePlayer currentPlayer) {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot draw from an empty pile");
        }
        CardGameCard drawn = topCard();
        state.println((currentPlayer.isNPC() ? currentPlayer.getName() : "You") +
                " draw " + drawn.getText() + " from the discard.");
        state.addHandAnimation(currentPlayer, false, true, false);
        remove(drawn);
        state.waitForAnimationToFinish();
        currentPlayer.giveCard(drawn, cardGame);
    }

    @Override
    public boolean hasSpecialCursor() {
        return false;
    }

    @Override
    public Sprite getCursorSprite() {
        return null;
    }
}
