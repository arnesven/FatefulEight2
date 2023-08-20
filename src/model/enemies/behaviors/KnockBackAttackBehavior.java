package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.Enemy;
import model.states.CombatEvent;
import util.MyRandom;

public class KnockBackAttackBehavior extends EnemyAttackBehavior {
    private int chance;

    public KnockBackAttackBehavior(int chance) {
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
        if (target.getHP() < hpBefore && !target.isDead() && MyRandom.rollD10() < this.chance) {
            if (model.getParty().getFrontRow().contains(target)) {
                model.getParty().getFrontRow().remove(target);
                model.getParty().getBackRow().add(target);
                combatEvent.println(target.getName() + " has been knocked back!");
            }
        }
    }
}
