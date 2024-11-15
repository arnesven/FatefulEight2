package model.enemies;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.enemies.behaviors.EnemyAttackBehavior;
import model.states.CombatEvent;

public abstract class GoblinEnemy extends Enemy {
    public GoblinEnemy(char enemyGroup, String name, EnemyAttackBehavior behavior) {
        super(enemyGroup, name, behavior);
    }

    @Override
    public int getMaxHP() {
        return 2;
    }

    @Override
    public int getSpeed() {
        return 2;
    }

    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    public String getDeathSound() {
        return null;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }

    public abstract GoblinEnemy copy();

    @Override
    public void doUponDeath(Model model, CombatEvent combatEvent, GameCharacter killer) {
        super.doUponDeath(model, combatEvent, killer);
        GameStatistics.incrementGoblinsKilled();
    }
}
