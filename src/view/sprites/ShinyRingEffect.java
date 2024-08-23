package view.sprites;

import view.MyColors;

public class ShinyRingEffect extends RunOnceAnimationSprite {
    public ShinyRingEffect() {
        super("shinyring", "combat.png", 0, 13, 32, 32, 8, MyColors.LIGHT_BLUE);
        setColor2(MyColors.WHITE);
    }
}
