package model.enemies;

import model.enemies.behaviors.MultiMagicAttackBehavior;

public class ElderDaemonEnemy extends DaemonEnemy {
    public ElderDaemonEnemy(char a) {
        super(a);
        setName("Elder Daemon");
        setAttackBehavior(new MultiMagicAttackBehavior(3));
    }

    @Override
    public int getMaxHP() {
        return 24;
    }

    @Override
    public int getSpeed() {
        return 6;
    }

    @Override
    public int getDamage() {
        return 6;
    }
}
