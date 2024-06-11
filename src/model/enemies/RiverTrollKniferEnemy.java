package model.enemies;

import model.enemies.behaviors.BleedAttackBehavior;
import view.MyColors;

public class RiverTrollKniferEnemy extends RiverTrollEnemy {
    public RiverTrollKniferEnemy(char c) {
        super(c, "Knifer", 0x108, MyColors.DARK_GRAY);
        setAttackBehavior(new BleedAttackBehavior(2));
    }

    @Override
    public int getSpeed() {
        return 3;
    }
}
