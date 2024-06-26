package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.ParalysisCondition;
import model.enemies.Enemy;
import model.states.CombatEvent;
import util.MyRandom;

public class ParalysisAttackBehavior extends EnemyAttackBehavior {
    private int chance;

    public ParalysisAttackBehavior(int chance) {
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
        if (hpBefore > target.getHP() && !target.isDead()) {
            if (MyRandom.rollD10() <= chance) {
                target.addCondition(new ParalysisCondition());
                if (target.hasCondition(ParalysisCondition.class)) {
                    combatEvent.println(target.getName() + " has been paralyzed!");
                    model.getTutorial().enemyAttacks(model);
                } else {
                    combatEvent.println(target.getName() + " was not paralyzed by the attack.");
                }
            }
        }
    }

    @Override
    public String getUnderText() {
        return "Paralysis";
    }
}
