package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.Enemy;
import model.states.CombatEvent;
import view.sprites.RangedMagicStrikeEffectSprite;
import view.sprites.RunOnceAnimationSprite;

public class MagicRangedAttackBehavior extends EnemyAttackBehavior {
    @Override
    public boolean canAttackBackRow() {
        return true;
    }

    @Override
    public boolean isPhysicalAttack() {
        return false;
    }

    @Override
    public String getUnderText() {
        return "Magic Ranged";
    }

    @Override
    public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
        model.getTutorial().enemyAttacks(model);
        super.performAttack(model, enemy, target, combatEvent);
    }

    @Override
    public String getSound() {
        return "wand";
    }

    @Override
    public RunOnceAnimationSprite getStrikeEffect() {
        return new RangedMagicStrikeEffectSprite();
    }

}
