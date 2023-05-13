package view.sprites;

import view.MyColors;

public class DownArrowAnimation extends RunOnceAnimationSprite {
    public DownArrowAnimation() {
        super("weakenarrow", "combat.png", 4, 12, 32, 32, 4, MyColors.LIGHT_PINK);
        setColor2(MyColors.LIGHT_RED);
        setAnimationDelay(6);
    }
}
