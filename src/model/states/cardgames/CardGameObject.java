package model.states.cardgames;

import model.Model;
import model.states.GameState;
import view.sprites.Sprite;

public interface CardGameObject {
    Sprite getSprite();
    String getText();
    void doAction(Model model, GameState state, CardGame cardGame, CardGamePlayer currentPlayer);
}
