package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.behaviors.EnemyAttackBehavior;
import model.enemies.behaviors.KnockDownAttackBehavior;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;

public class YellowGelatinousBlobEnemy extends GelatinousBlobEnemy {

    public YellowGelatinousBlobEnemy(char a) {
        super(a, MyColors.LIGHT_YELLOW, MyColors.YELLOW);
        setAttackBehavior(new ShockBlobAttackBehavior());
    }

    @Override
    public GelatinousBlobEnemy copy() {
        return new YellowGelatinousBlobEnemy(getEnemyGroup());
    }

    private static class ShockAttackBehavior extends KnockDownAttackBehavior {
        public ShockAttackBehavior() {
            super(9);
        }

        @Override
        protected String getVerb() {
            return "stunned";
        }
    }

    private static class ShockBlobAttackBehavior extends BlobAttackBehavior {

        private final ShockAttackBehavior innerBehavior;

        public ShockBlobAttackBehavior() {
            super(5);
            this.innerBehavior = new ShockAttackBehavior();
        }

        @Override
        public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
            if (MyRandom.rollD6() == 1) {
                combatEvent.println(enemy.getName() + " attacks " + target.getName() + " with a powerful electric shock!");
                innerBehavior.performAttack(model, enemy, target, combatEvent);
            } else {
                super.performAttack(model, enemy, target, combatEvent);
            }
        }
    }
}
