package view.sprites;

import view.MyColors;

public interface ArrowSprites {
    Sprite RIGHT = CharSprite.make(0xB0, MyColors.BLACK, MyColors.WHITE, MyColors.BLUE);
    Sprite LEFT = CharSprite.make(0xB1, MyColors.BLACK, MyColors.WHITE, MyColors.BLUE);
    Sprite RIGHT_BLACK = CharSprite.make(0xB0, MyColors.BLACK, MyColors.WHITE, MyColors.BLACK);
}
