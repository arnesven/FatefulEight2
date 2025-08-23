package view.sprites;

import model.Model;
import view.MyColors;

public class MagmaBlastEffectSprite extends RunOnceAnimationSprite {
    private int shift = 48;

    public MagmaBlastEffectSprite() {
        super("magmablasteffect", "combat.png", 0, 11, 32, 32, 8, MyColors.RED);
        setColor2(MyColors.ORANGE);
    }

    @Override
    public int getYShift() {
        return shift;
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        super.stepAnimation(elapsedTimeMs, model);
        shift -= 2;
    }
}
