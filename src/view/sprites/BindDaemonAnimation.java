package view.sprites;

import view.MyColors;

public class BindDaemonAnimation extends RunOnceAnimationSprite {
    public BindDaemonAnimation() {
        super("binddeamon", "combat.png", 0, 17, 32, 32, 8, MyColors.WHITE);
        setColor2(MyColors.WHITE);
    }
}
