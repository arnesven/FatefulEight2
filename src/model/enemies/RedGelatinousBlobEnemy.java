package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.behaviors.BleedAttackBehavior;
import model.enemies.behaviors.EnemyAttackBehavior;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;

public class RedGelatinousBlobEnemy extends GelatinousBlobEnemy {

    public RedGelatinousBlobEnemy(char a) {
        super(a, MyColors.RED, MyColors.DARK_RED);
        setAttackBehavior(new BleedBlobAttackBehavior());
    }

    @Override
    public GelatinousBlobEnemy copy() {
        return new RedGelatinousBlobEnemy(getEnemyGroup());
    }

    private class BleedBlobAttackBehavior extends BlobAttackBehavior {
        private final BleedAttackBehavior inner;

        public BleedBlobAttackBehavior() {
            super(5);
            this.inner = new BleedAttackBehavior(9);
        }

        @Override
        public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
            if (MyRandom.rollD6() == 1) {
                combatEvent.println(getName() + " suddenly shoot outs a burst of metal shards at " + target.getName() + "!");
                inner.performAttack(model, enemy, target, combatEvent);
            } else {
                super.performAttack(model, enemy, target, combatEvent);
            }
        }
    }
}
