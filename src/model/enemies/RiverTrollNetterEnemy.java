package model.enemies;

import model.enemies.behaviors.KnockDownAttackBehavior;
import view.MyColors;

public class RiverTrollNetterEnemy extends RiverTrollEnemy {
    public RiverTrollNetterEnemy(char b) {
        super(b, "Netter", 0x100, MyColors.BROWN);
        setAttackBehavior(new KnockDownAttackBehavior(2));
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    public int getSpeed() {
        return -1;
    }
}
