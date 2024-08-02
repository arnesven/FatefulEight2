package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.behaviors.PoisonAttackBehavior;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;

public class GreenGelatinousBlobEnemy extends GelatinousBlobEnemy {

    public GreenGelatinousBlobEnemy(char a) {
        super(a, MyColors.GREEN, MyColors.DARK_GREEN);
        setAttackBehavior(new PoisonBlobAttackBehavior());
    }

    @Override
    public GelatinousBlobEnemy copy() {
        return new GreenGelatinousBlobEnemy(getEnemyGroup());
    }

    private static class PoisonBlobAttackBehavior extends BlobAttackBehavior {
        private final PoisonAttackBehavior inner;
        public PoisonBlobAttackBehavior() {
            super(5);
            this.inner = new PoisonAttackBehavior(9);
        }

        @Override
        public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
            if (MyRandom.rollD6() == 1) {
                combatEvent.println(enemy.getName() + " attacked " + target.getName() + " with a putrid oozing goo!");
                inner.performAttack(model, enemy, target, combatEvent);
            } else {
                super.performAttack(model, enemy, target, combatEvent);
            }
        }
    }
}
