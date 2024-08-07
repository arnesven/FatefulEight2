package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.Enemy;
import model.states.CombatEvent;
import util.MyRandom;

public class PullForwardAttackBehavior extends EnemyAttackBehavior {

    @Override
    public boolean canAttackBackRow() {
        return true;
    }

    @Override
    public int calculateDamage(Enemy enemy, boolean isRanged) {
        if (isRanged) {
            return enemy.getDamage() - 1;
        }
        return enemy.getDamage();
    }

    @Override
    public String getUnderText() {
        return "Pull Forward";
    }

    @Override
    public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
        int hpBefore = target.getHP();
        super.performAttack(model, enemy, target, combatEvent);
        int chance = 7;
        if (target.getHP() < hpBefore && !target.isDead() && MyRandom.rollD10() < chance) {
            if (model.getParty().getBackRow().contains(target)) {
                combatEvent.toggleFormationFor(model, target);
                combatEvent.println(target.getName() + " has been pulled forward!");
                model.getTutorial().enemyAttacks2(model);
            }
        }
    }
}
