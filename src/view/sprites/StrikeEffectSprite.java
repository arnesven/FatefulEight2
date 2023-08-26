package view.sprites;

import view.MyColors;

public class StrikeEffectSprite extends RunOnceAnimationSprite {
    public StrikeEffectSprite(MyColors color) {
        super("strikeeffectsprite", "combat.png", 0, 3, 32, 32, 8, color);
        setColor2(MyColors.RED);
        setColor3(MyColors.WHITE);
    }

    public StrikeEffectSprite() {
        this(MyColors.WHITE);
    }
}
