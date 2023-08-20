package model.enemies;

import model.enemies.behaviors.MeleeAttackBehavior;

public class QuadMinionEnemy extends CultistEnemy {
    public QuadMinionEnemy(char a) {
        super(a);
        setName("Quad Minion");
        setAttackBehavior(new MeleeAttackBehavior());
    }
}
