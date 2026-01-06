package model.mainstory.jungletribe;

import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class CCWRubiqButton extends RubiqButton {
    private static final Sprite SPRITE = new CCWRubiqButtonSprite();

    public CCWRubiqButton(int[] indices, boolean clockwise, String description) {
        super(indices, clockwise, description);
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, Point point) {
        screenHandler.register(SPRITE.getName(), point, SPRITE, 2);
    }

    private static class CCWRubiqButtonSprite extends Sprite32x32 {
        public CCWRubiqButtonSprite() {
            super("rubiqbutton", "quest.png", 0xDE,
                    MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY_RED, MyColors.BEIGE);
            setFlipHorizontal(true);
        }
    }
}
