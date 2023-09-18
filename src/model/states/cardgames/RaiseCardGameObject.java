package model.states.cardgames;

import model.Model;
import view.MyColors;
import view.sprites.CardGameButtonSprite;
import view.sprites.Sprite;

public class RaiseCardGameObject extends ButtonCardGameObject {

    private static final Sprite SPRITE = new CardGameButtonSprite(0x13, MyColors.LIGHT_GREEN);

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getText() {
        return "Raise the current bet.";
    }

    @Override
    public void doAction(Model model, CardGameState state, CardGame cardGame, CardGamePlayer currentPlayer) {
        state.println((currentPlayer.isNPC() ? currentPlayer.getName() : "You") + " raise the current bet.");
        state.addHandAnimation(currentPlayer, false, false, true);
        state.waitForAnimationToFinish();
        int bet = 1; // TODO: Raise more, but not more than maximum bet.
        currentPlayer.addToBet(bet);
        cardGame.addToCurrentBet(bet);
    }

}
