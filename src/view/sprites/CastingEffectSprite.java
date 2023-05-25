package view.sprites;

import view.MyColors;

public class CastingEffectSprite extends RunOnceAnimationSprite {
    public CastingEffectSprite() {
        super("castingeffect", "combat.png", 0, 9, 32, 32, 8, MyColors.WHITE);
        setColor1(MyColors.WHITE);
        setColor2(MyColors.LIGHT_YELLOW);
        setColor3(MyColors.LIGHT_PINK);
        setColor4(MyColors.CYAN);
    }
}
