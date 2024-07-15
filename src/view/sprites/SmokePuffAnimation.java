package view.sprites;

import view.MyColors;

public class SmokePuffAnimation extends RunOnceAnimationSprite {
    public SmokePuffAnimation() {
        super("smokepuff", "dungeon.png", 0, 17, 32, 32, 7, MyColors.LIGHT_GRAY);
        setColor2(MyColors.LIGHT_GRAY);
    }
}
