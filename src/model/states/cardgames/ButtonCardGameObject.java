package model.states.cardgames;

import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public abstract class ButtonCardGameObject implements CardGameObject {

    private static final Sprite CURSOR = new ButtonCursorSprite();

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
