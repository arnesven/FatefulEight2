package view.sprites;

import model.Model;
import view.MyColors;

public class EntropicBoltEffect extends RunOnceAnimationSprite {
    private int shift = 48;

    public EntropicBoltEffect(MyColors color) {
        super("entropicbolt", "combat.png", 0, 12, 32, 32, 4, MyColors.WHITE);
        setColor2(color);
    }

    @Override
    public int getYShift() {
        return shift;
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        super.stepAnimation(elapsedTimeMs, model);
        shift -= 4;
    }
}
