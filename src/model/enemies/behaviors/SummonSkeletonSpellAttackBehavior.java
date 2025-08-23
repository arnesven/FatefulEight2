package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.*;
import model.states.CombatEvent;
import util.MyRandom;

public class SummonSkeletonSpellAttackBehavior extends SpellAttackBehavior {
    public SummonSkeletonSpellAttackBehavior() {
        super("Rise Deathly Minion", 1);
    }

    @Override
    protected void resolveSpell(Model model, Enemy enemy, GameCharacter target, CombatEvent combat) {
        Enemy newEnemy;
        int dieRoll = MyRandom.rollD6();
        if (dieRoll < 3) {
            newEnemy = new GhostEnemy(enemy.getEnemyGroup());
        } else if (dieRoll < 5) {
            newEnemy = new MummyEnemy(enemy.getEnemyGroup());
        } else if (dieRoll < 7) {
            newEnemy = new GhoulEnemy(enemy.getEnemyGroup());
        } else {
            newEnemy = new SkeletonEnemy(enemy.getEnemyGroup());
        }
        combat.addEnemy(newEnemy);
    }
}
