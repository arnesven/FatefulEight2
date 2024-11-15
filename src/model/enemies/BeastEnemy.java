package model.enemies;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.enemies.behaviors.EnemyAttackBehavior;
import model.states.CombatEvent;

public abstract class BeastEnemy extends Enemy {
    public static final int DOCILE = 1;
    public static final int NORMAL = 2;
    public static final int HOSTILE = 3;
    public static final int RAMPAGING = 4;
    private int aggro;

    public BeastEnemy(char enemyGroup, String name, int aggressiveness, EnemyAttackBehavior attackBehavior) {
        super(enemyGroup, name, attackBehavior);
        this.aggro = aggressiveness;
    }

    public int getAggressiveness() {
        return aggro;
    }

    @Override
    public void takeCombatDamage(CombatEvent combatEvent, int damage, GameCharacter damager) {
        super.takeCombatDamage(combatEvent, damage, damager);
        if (aggro < RAMPAGING && !isDead()) {
            increaseAggressiveness();
            combatEvent.println(getName() + " got angrier.");
        }
    }

    public boolean reduceAggressiveness() {
        if (aggro > DOCILE) {
            aggro--;
            return true;
        }
        return false;
    }

    public boolean increaseAggressiveness() {
        if (aggro < RAMPAGING) {
            aggro++;
            return true;
        }
        return false;
    }

    @Override
    public String getDeathSound() {
        return "beast_death";
    }

    @Override
    public void doUponDeath(Model model, CombatEvent combatEvent, GameCharacter killer) {
        super.doUponDeath(model, combatEvent, killer);
        GameStatistics.incrementBeastsKilled();
    }
}
