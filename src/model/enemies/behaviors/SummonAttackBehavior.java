package model.enemies.behaviors;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.combat.conditions.SummonCondition;
import model.enemies.Enemy;
import model.states.CombatEvent;
import view.sprites.SmokeBallAnimation;

import java.util.ArrayList;
import java.util.List;

public abstract class SummonAttackBehavior extends EnemyAttackBehavior {

    private List<Enemy> summons = new ArrayList<>();

    @Override
    public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
        Enemy e = makeEnemy(model);
        combatEvent.addEnemy(e);
        summons.add(e);
        combatEvent.addSpecialEffect(e, new SmokeBallAnimation());
    }

    protected abstract Enemy makeEnemy(Model model);

    @Override
    public boolean canAttackBackRow() {
        return true;
    }

    @Override
    public String getUnderText() {
        return "Summon";
    }

    @Override
    public void announceRangedAttack(Model model, CombatEvent combatEvent, Enemy enemy, GameCharacter target) {
        // Don't do that.
    }

    public void removeSummons(Enemy summoner, CombatEvent combatEvent) {
        combatEvent.println(summoner.getName() + "'s summons disappear!");
        for (Enemy e : summons) {
            combatEvent.retreatEnemy(e);
        }
    }
}
