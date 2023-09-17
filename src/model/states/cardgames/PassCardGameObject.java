package model.states.cardgames;

import model.Model;
import model.states.CardGameState;
import view.MyColors;
import view.sprites.CardGameButtonSprite;
import view.sprites.Sprite;

public class PassCardGameObject extends ButtonCardGameObject {

    private static final Sprite SPRITE = new CardGameButtonSprite(0x22, MyColors.BEIGE);

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getText() {
        return "Finish your turn.";
    }

    @Override
    public void doAction(Model model, CardGameState state, CardGame cardGame, CardGamePlayer currentPlayer) {

    }
}
