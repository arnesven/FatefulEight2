package view.sprites;

import view.MyColors;

public class ExplosionAnimation extends RunOnceAnimationSprite {
    public ExplosionAnimation() {
        super("explosion", "dungeon.png", 0, 8, 32, 32, 6, MyColors.ORANGE);
        setColor2(MyColors.WHITE);
        setColor3(MyColors.YELLOW);
    }
}
