package model.enemies;

import model.Model;
import model.combat.BossCombatLoot;
import model.combat.CombatLoot;
import model.enemies.behaviors.MagicRangedAttackBehavior;

public class UndeadLordEnemy extends SkeletonEnemy {
    public UndeadLordEnemy(char a) {
        super(a);
        setAttackBehavior(new MagicRangedAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 14;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new BossCombatLoot(model);
    }
}
