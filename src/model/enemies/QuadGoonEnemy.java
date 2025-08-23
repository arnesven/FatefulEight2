package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.enemies.behaviors.EnemyAttackBehavior;

public abstract class QuadGoonEnemy extends HumanoidEnemy {

    public QuadGoonEnemy(char group, String name, EnemyAttackBehavior attackBehavior) {
        super(group, name, attackBehavior);
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model); // TODO QuadGoonLoot
    }
}
