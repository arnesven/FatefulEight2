package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.Enemy;
import model.states.CombatEvent;
import util.MyRandom;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.StrikeEffectSprite;

public abstract class EnemyAttackBehavior {
    public abstract boolean canAttackBackRow();

    public int calculateDamage(Enemy enemy, boolean isRanged) {
        return enemy.getDamage();
    }

    public boolean isCriticalHit() {
        return MyRandom.rollD10() == 10;
    }

    public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
        target.getAttackedBy(enemy, model, combatEvent);
    }

    public RunOnceAnimationSprite getStrikeEffect() {
        return new StrikeEffectSprite();
    }

    public boolean allowsDamageReduction() {
        return true;
    }

    public abstract String getUnderText();
}
