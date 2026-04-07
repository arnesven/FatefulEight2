package view.sprites;

import view.MyColors;

public class BreakingRockAnimation extends RunOnceAnimationSprite {
    public BreakingRockAnimation(MyColors color) {
        super("breakingrock", "warehouse.png", 0, 4, 32, 32, 3, MyColors.DARK_GRAY);
        setColor2(color);
    }
}
