package view.sprites;

import view.MyColors;

public class StrikeEffectSprite extends RunOnceAnimationSprite {
    public StrikeEffectSprite() {
        super("strikeeffectsprite", "combat.png", 0, 3, 32, 32, 8, MyColors.WHITE);
        setColor2(MyColors.RED);
        setColor3(MyColors.WHITE);
    }
}
