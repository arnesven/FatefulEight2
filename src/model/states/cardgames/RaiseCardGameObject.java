package model.states.cardgames;

import model.Model;
import model.states.CardGameState;
import view.MyColors;
import view.sprites.CardGameButtonSprite;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.util.List;

public class RaiseCardGameObject implements CardGameObject {

    private static final Sprite SPRITE = new CardGameButtonSprite(0x13, MyColors.BEIGE);
    private static final Sprite CURSOR = new ButtonCursorSprite();

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
        currentPlayer.addToBet(1);
    }

    @Override
    public boolean hasSpecialCursor() {
        return true;
    }

    @Override
    public Sprite getCursorSprite() {
        return CURSOR;
    }

    private static class ButtonCursorSprite extends LoopingSprite {
        public ButtonCursorSprite() {
            super("cardgamebuttoncursor", "cardgame.png", 0x23, 32, 16);
            setFrames(2);
            setColor1(MyColors.YELLOW);
        }
    }
}
