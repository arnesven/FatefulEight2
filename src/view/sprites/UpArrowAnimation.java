package view.sprites;

import view.MyColors;

public class UpArrowAnimation extends RunOnceAnimationSprite {
    public UpArrowAnimation() {
        super("weakenarrow", "combat.png", 4, 15, 32, 32, 4, MyColors.CYAN);
        setColor2(MyColors.LIGHT_BLUE);
        setAnimationDelay(6);
    }
}
