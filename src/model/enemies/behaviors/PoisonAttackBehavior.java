package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.combat.PoisonCondition;
import model.enemies.Enemy;
import model.states.CombatEvent;
import util.MyRandom;

public class PoisonAttackBehavior extends EnemyAttackBehavior {
    private int chance;

    public PoisonAttackBehavior(int chance) {
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
            combatEvent.println(target.getName() + " has been poisoned!");
            target.addCondition(new PoisonCondition());
        }
    }

    @Override
    public String getUnderText() {
        return "Poison";
    }
}
