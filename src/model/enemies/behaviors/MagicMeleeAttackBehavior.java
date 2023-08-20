package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.Enemy;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.RunOnceAnimationSprite;

public class MagicMeleeAttackBehavior extends MeleeAttackBehavior {
    @Override
    public boolean isPhysicalAttack() {
        return false;
    }

    @Override
    public RunOnceAnimationSprite getStrikeEffect() {
        return new MagicMeleeStrikeEffectSprite();
    }

    private static class MagicMeleeStrikeEffectSprite extends RunOnceAnimationSprite {
        public MagicMeleeStrikeEffectSprite() {
            super("strikeeffectsprite", "combat.png", 0, 3, 32, 32, 8, MyColors.LIGHT_RED);
        }
    }

    @Override
    public String getUnderText() {
        return "Magic";
    }

    @Override
    public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
        model.getTutorial().enemyAttacks(model);
        super.performAttack(model, enemy, target, combatEvent);
    }
}
