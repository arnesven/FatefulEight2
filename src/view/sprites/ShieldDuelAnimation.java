package view.sprites;

import view.MyColors;

public class ShieldDuelAnimation extends RunOnceAnimationSprite {
    public ShieldDuelAnimation() {
        super("shieldduel", "gauge.png", 0, 1, 32, 32, 3, MyColors.WHITE);
        setAnimationDelay(5);
    }
}
