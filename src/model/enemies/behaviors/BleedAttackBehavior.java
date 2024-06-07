package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.BleedingCondition;
import model.enemies.Enemy;
import model.states.CombatEvent;
import util.MyRandom;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.SlashStrikeEffectSprite;

public class BleedAttackBehavior extends EnemyAttackBehavior {
    private final int chance;

    public BleedAttackBehavior(int chance) {
        this.chance = chance;
    }

    @Override
    public boolean canAttackBackRow() {
        return false;
    }

    @Override
    public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
        int hpBefore = target.getHP();
        super.performAttack(model, enemy, target, combatEvent);
        if (hpBefore > target.getHP() && !target.isDead() && MyRandom.rollD10() <= chance) {
            target.addCondition(new BleedingCondition());
            if (target.hasCondition(BleedingCondition.class)) {
                combatEvent.println(target.getName() + " is bleeding!");
                model.getTutorial().enemyAttacks(model);
            } else {
                combatEvent.println(target.getName() + " does not start bleeding from the attack.");
            }
        }
    }

    @Override
    public String getUnderText() {
        return "Bleeding";
    }

    @Override
    public RunOnceAnimationSprite getStrikeEffect() {
        return new SlashStrikeEffectSprite();
    }
}
