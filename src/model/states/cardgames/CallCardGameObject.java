package model.states.cardgames;

import model.Model;
import model.states.CardGameState;
import view.MyColors;
import view.sprites.CardGameButtonSprite;
import view.sprites.Sprite;

public class CallCardGameObject extends ButtonCardGameObject {

    private static final Sprite SPRITE = new CardGameButtonSprite(0x15, MyColors.CYAN);

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getText() {
        return "Call current bet.";
    }

    @Override
    public void doAction(Model model, CardGameState state, CardGame cardGame, CardGamePlayer currentPlayer) {
        if (currentPlayer.isNPC()) {
            state.println(currentPlayer.getName() + " calls.");
        } else {
            state.println("You call.");
        }
        state.addHandAnimation(currentPlayer, false, false, true);
        state.waitForAnimationToFinish();
        int toBet = cardGame.getCurrentBet() - currentPlayer.getBet();
        currentPlayer.addToBet(toBet);
    }
}
