package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.*;
import model.enemies.behaviors.MeleeAttackBehavior;
import model.states.CombatEvent;
import util.MyRandom;
import view.sprites.SmokePuffAnimation;

public class VampireAttackBehavior extends MeleeAttackBehavior {
    @Override
    public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
        if (enemy.hasCondition(EnemyBatFormCondition.class)) {
            combatEvent.retreatEnemy(enemy);
            combatEvent.println(enemy.getName() + " escaped from combat.");
        }
        if (enemy.getHP() < 3) {
            combatEvent.println(enemy.getName() + " turns into a bat.");
            combatEvent.addSpecialEffect(enemy, new SmokePuffAnimation());
            enemy.addCondition(new EnemyBatFormCondition());
        } else {
            int hpBefore = target.getHP();
            super.performAttack(model, enemy, target, combatEvent);
            if (!target.isDead() && hpBefore > target.getHP() && MyRandom.rollD10() == 10) {
                VampirismCondition.makeVampire(model, combatEvent, target);
            }
        }
    }

}
