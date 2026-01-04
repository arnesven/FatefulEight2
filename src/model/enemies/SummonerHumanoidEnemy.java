package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.behaviors.SummonAttackBehavior;
import model.states.CombatEvent;

public abstract class SummonerHumanoidEnemy extends HumanoidEnemy {

    private final SummonAttackBehavior behavior;

    public SummonerHumanoidEnemy(char group, String name, SummonAttackBehavior summonBehavior) {
        super(group, name, summonBehavior);
        this.behavior = summonBehavior;
    }

    @Override
    public void doUponDeath(Model model, CombatEvent combatEvent, GameCharacter killer) {
        behavior.removeSummons(this, combatEvent);
    }
}
