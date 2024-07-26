package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.Enemy;
import model.states.CombatEvent;

public class NoAttackCombatBehavior extends EnemyAttackBehavior {

    @Override
    public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) { }

    @Override
    public boolean canAttackBackRow() {
        return false;
    }

    @Override
    public String getUnderText() {
        return "No attack";
    }
}
