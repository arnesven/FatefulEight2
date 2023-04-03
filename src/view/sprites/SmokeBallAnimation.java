package view.sprites;

import view.MyColors;

public class SmokeBallAnimation extends RunOnceAnimationSprite {
    public SmokeBallAnimation() {
        super("smokeball", "combat.png", 0, 16, 32, 32, 8, MyColors.LIGHT_GRAY);
        setColor1(MyColors.WHITE);
    }
}
