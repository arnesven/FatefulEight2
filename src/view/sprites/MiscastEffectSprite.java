package view.sprites;

import view.MyColors;

public class MiscastEffectSprite extends RunOnceAnimationSprite {
    public MiscastEffectSprite() {
        super("miscasteffect", "combat.png", 0, 10, 32, 32, 8, MyColors.WHITE);
        setColor1(MyColors.GRAY);
        setColor2(MyColors.LIGHT_GRAY);
        setColor3(MyColors.WHITE);
        setColor4(MyColors.TAN);
    }
}
