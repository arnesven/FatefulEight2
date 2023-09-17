package model.states.cardgames;

import model.Model;
import model.states.CardGameState;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

public interface CardGameObject {
    Sprite getSprite();
    String getText();
    void doAction(Model model, CardGameState state, CardGame cardGame, CardGamePlayer currentPlayer);
    boolean hasSpecialCursor();
    Sprite getCursorSprite();
    Sprite BLANK_CARD_SPRITE = new Sprite16x16("blankcard", "cardgame.png", 0x21);
}
