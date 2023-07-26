package view.sprites;

import model.Model;
import view.MyColors;

public interface ArrowSprites {
    Sprite RIGHT = CharSprite.make(0xB0, MyColors.BLACK, MyColors.WHITE, MyColors.BLUE);
    Sprite LEFT = CharSprite.make(0xB1, MyColors.BLACK, MyColors.WHITE, MyColors.BLUE);
    Sprite RIGHT_BLACK = CharSprite.make(0xB0, MyColors.BLACK, MyColors.WHITE, MyColors.BLACK);
    Sprite RIGHT_BLACK_BLINK = new BlinkingRightArrow();

    class BlinkingRightArrow extends LoopingSprite {

        public BlinkingRightArrow() {
            super("blinkrightarro", "animations.png", 0x0, 8, 8);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.WHITE);
            setColor3(MyColors.BLACK);
            setFrames(2);
        }

    }
}
