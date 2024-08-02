package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.TimedParalysisCondition;
import model.enemies.Enemy;
import model.states.CombatEvent;
import util.MyRandom;

public class KnockDownAttackBehavior extends EnemyAttackBehavior {
    private int chance;

    public KnockDownAttackBehavior(int chance) {
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
        if (!target.isDead() && hpBefore > target.getHP() && MyRandom.rollD10() <= chance) {
            combatEvent.println(target.getName() + " has been " + getVerb() + " by " + enemy.getName() + ".");
            target.addCondition(new TimedParalysisCondition());
            model.getTutorial().enemyAttacks2(model);
        }
    }

    protected String getVerb() {
        return "knocked down";
    }

    @Override
    public String getUnderText() {
        return "Knock Down";
    }
}
