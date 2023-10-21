package view.sprites;

import model.Model;
import view.MyColors;

public class DamageValueEffect extends RunOnceAnimationSprite {
    public static final MyColors STANDARD_DAMAGE = MyColors.BLACK;
    public static final MyColors CRITICAL_DAMAGE = MyColors.RED;
    public static final MyColors MAGICAL_DAMAGE = MyColors.PURPLE;
    public static final MyColors HEALING = MyColors.GREEN;

    private int count;
    private int shift;

    public DamageValueEffect(int amount, MyColors color) {
        super("floatingdamage", "charset.png",
                (0xF0+amount) % 16, 0xE0 / 16, 8, 8, 1, STANDARD_DAMAGE);
        setColor2(MyColors.WHITE);
        this.count = 0;
        this.shift = 0;
        if (color != STANDARD_DAMAGE) {
            setColor1(color);
        }
    }

    public DamageValueEffect(int num) {
        super("floatingtext" + num, "charset.png",
                num % 16, num / 16, 8, 8, 1, MyColors.CYAN);
        setColor2(MyColors.DARK_BLUE);
        this.count = 0;
        this.shift = 0;
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
