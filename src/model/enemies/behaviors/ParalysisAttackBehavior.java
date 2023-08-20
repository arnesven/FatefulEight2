package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.combat.ParalysisCondition;
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
                combatEvent.println(target.getName() + " has been paralyzed!");
                target.addCondition(new ParalysisCondition());
            }
        }
    }
}
