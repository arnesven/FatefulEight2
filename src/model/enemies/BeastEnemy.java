package model.enemies;

import model.states.CombatEvent;

public abstract class BeastEnemy extends Enemy {
    public static final int DOCILE = 1;
    public static final int NORMAL = 2;
    public static final int HOSTILE = 3;
    public static final int RAMPAGING = 4;
    private int aggro;

    public BeastEnemy(char enemyGroup, String name, int aggressiveness) {
        super(enemyGroup, name);
        this.aggro = aggressiveness;
    }

    public int getAggressiveness() {
        return aggro;
    }

    @Override
    public void takeCombatDamage(CombatEvent combatEvent, int damage) {
        super.takeCombatDamage(combatEvent, damage);
        if (aggro < HOSTILE && !isDead()) {
            aggro++;
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
}
