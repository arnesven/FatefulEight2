package view.sprites;

import model.Model;
import view.MyColors;

public class RangedStrikeEffect extends RunOnceAnimationSprite {
    private static final int INITIAL_SHIFT = 48;
    private int shift = INITIAL_SHIFT;

    public RangedStrikeEffect() {
        super("rangedstrike", "combat.png", 12, 0, 32, 32, 4, MyColors.WHITE);
    }

    @Override
    public int getYShift() {
        return shift;
    }

    @Override
    public void reset() {
        super.reset();
        shift = INITIAL_SHIFT;
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        super.stepAnimation(elapsedTimeMs, model);
        shift -= 4;
    }
}
