package model.enemies;

import model.enemies.behaviors.EnemyAttackBehavior;

public abstract class HumanoidEnemy extends Enemy {

    public HumanoidEnemy(char group, String name) {
        super(group, name);
    }

    public HumanoidEnemy(char group, String name, EnemyAttackBehavior attackBehavior) {
        super(group, name, attackBehavior);
    }

    @Override
    public String getDeathSound() {
        return "human_enemy_death";
    }
}
