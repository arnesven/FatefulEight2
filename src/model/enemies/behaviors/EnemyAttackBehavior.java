package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.Enemy;
import model.states.CombatEvent;
import util.MyRandom;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.StrikeEffectSprite;

import java.io.Serializable;

public abstract class EnemyAttackBehavior implements Serializable {
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

    public boolean isPhysicalAttack() {
        return true;
    }

    public abstract String getUnderText();

    public int numberOfAttacks() {
        return 1;
    }

    public String getSound() {
        return "default_attack";
    }
}
