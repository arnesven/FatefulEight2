package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.DamageValueEffect;

public class PinkGelatinousBlobEnemy extends GelatinousBlobEnemy {
    public PinkGelatinousBlobEnemy(char a) {
        super(a, MyColors.LIGHT_PINK, MyColors.PINK);
        setAttackBehavior(new RegeneratingBlobAttackBehavior());
    }

    @Override
    public GelatinousBlobEnemy copy() {
        return new PinkGelatinousBlobEnemy(getEnemyGroup());
    }

    private static class RegeneratingBlobAttackBehavior extends BlobAttackBehavior {
        public RegeneratingBlobAttackBehavior() {
            super(6);
        }

        @Override
        public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
            super.performAttack(model, enemy, target, combatEvent);
            if (!enemy.isDead() && enemy.getHP() < enemy.getMaxHP()) {
                combatEvent.println(enemy.getName() + " regenerated.");
                int regen = Math.min(2, enemy.getMaxHP() - enemy.getHP());
                enemy.addToHP(regen);
                combatEvent.addFloatyDamage(enemy, regen, DamageValueEffect.HEALING);
            }
        }
    }
}
