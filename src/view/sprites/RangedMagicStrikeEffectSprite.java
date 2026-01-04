package view.sprites;

import model.Model;
import view.MyColors;

public class RangedMagicStrikeEffectSprite extends RunOnceAnimationSprite {
    private int shift = -64;

    public RangedMagicStrikeEffectSprite(MyColors color) {
        super("enemyrangedmagiceffect", "combat.png", 8, 2, 32, 32, 4, MyColors.WHITE);
        setColor2(MyColors.LIGHT_RED);
    }

    public RangedMagicStrikeEffectSprite() {
        this(MyColors.LIGHT_RED);
    }

    @Override
    public int getYShift() {
        return shift;
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        super.stepAnimation(elapsedTimeMs, model);
        shift += 4;
    }
}
