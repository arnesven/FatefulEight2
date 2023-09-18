package model.states.cardgames;

import model.Model;
import view.MyColors;
import view.sprites.CardGameButtonSprite;
import view.sprites.Sprite;

public class FoldCardGameObject extends ButtonCardGameObject {

    private static final Sprite SPRITE = new CardGameButtonSprite(0x14, MyColors.LIGHT_RED);

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getText() {
        return "Fold your hand.";
    }

    @Override
    public void doAction(Model model, CardGameState state, CardGame cardGame, CardGamePlayer currentPlayer) {
        if (currentPlayer.isNPC()) {
            state.println(currentPlayer.getName() + " folds.");
        } else {
            state.println("You fold your hand.");
        }
        cardGame.foldPlayer(model, state, currentPlayer);
    }
}
