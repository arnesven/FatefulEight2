package view.sprites;

import view.MyColors;

public class MiniPictureSprite extends Sprite {
    public MiniPictureSprite(int num) {
        super("minipic"+num, "minipics.png", num % 16, num/16, 120, 96);
        MyColors.transformImage(this);
    }
}
