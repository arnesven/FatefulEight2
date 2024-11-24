package model.enemies;

import model.enemies.behaviors.RangedAttackBehavior;

public class SmugglerEnemy extends BrotherhoodCronyEnemy {
    public SmugglerEnemy(char a) {
        super(a);
        setName("Smuggler");
        setAttackBehavior(new RangedAttackBehavior());
    }
}
