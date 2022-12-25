package view.sprites;

import model.Model;
import view.MyColors;

public class DamageValueEffect extends RunOnceAnimationSprite {
    private int count;
    private int shift;

    public DamageValueEffect(int amount, boolean critical) {
        super("floatingdamage", "charset.png",
                (0xF0+amount) % 16, 0xE0 / 16, 8, 8, 1, MyColors.LIGHT_YELLOW);
        this.count = 0;
        this.shift = 0;
        if (critical) {
            setColor1(MyColors.LIGHT_RED);
        }
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        this.count++;
        if (this.count % 4 == 0) {
            shift++;
        }
    }

    @Override
    public int getYShift() {
        return shift;
    }

    @Override
    public int getXShift() {
        return 10;
    }

    @Override
    public boolean isDone() {
        return getYShift() > 20;
    }
}
