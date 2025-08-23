package view.sprites;

import model.Model;
import view.MyColors;

public class PhantasmEffect extends RunOnceAnimationSprite {
    private int shift = 0;

    public PhantasmEffect() {
        super("phantasmeffect", "combat.png", 2, 8, 32, 32, 6, MyColors.WHITE);
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        super.stepAnimation(elapsedTimeMs, model);
        shift += 1;
    }

    @Override
    public int getYShift() {
        return shift;
    }
}
