package view.sprites;

import model.Model;
import view.MyColors;

public class DrainLifeEffect extends RunOnceAnimationSprite {
    private int shift = -48;

    public DrainLifeEffect() {
        super("vampirism", "combat.png", 8, 7, 32, 32, 5, MyColors.DARK_RED);
        setColor2(MyColors.RED);
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
