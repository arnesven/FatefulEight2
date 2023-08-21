package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.combat.TimedParalysisCondition;
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
            combatEvent.println(target.getName() + " has been knocked down by " + enemy.getName() + ".");
            // TODO: trigger tutorial
            target.addCondition(new TimedParalysisCondition());
        }
    }

    @Override
    public String getUnderText() {
        return "Knock Down";
    }
}
