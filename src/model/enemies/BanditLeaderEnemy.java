package model.enemies;

import model.enemies.behaviors.MultiAttackBehavior;

public class BanditLeaderEnemy extends BanditEnemy {
    public BanditLeaderEnemy(char c) {
        super(c);
        setName("Bandit Leader");
        setAttackBehavior(new MultiAttackBehavior(2));
    }

    @Override
    public int getPhysicalDamageReduction() {
        return 1;
    }
}
