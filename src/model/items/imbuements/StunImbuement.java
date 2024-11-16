package model.items.imbuements;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.TimedParalysisCondition;
import model.enemies.HumanoidEnemy;
import model.states.CombatEvent;
import util.MyRandom;

public class StunImbuement extends WeaponImbuement {
    @Override
    public String getText() {
        return "30% chance of stunning humanoid opponents for 1 turn";
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        if (!target.isDead() && target instanceof HumanoidEnemy && MyRandom.rollD10() <= 3) {
            combatEvent.println(gameCharacter.getName() + " stunned " + target.getName() + ".");
            target.addCondition(new TimedParalysisCondition(1));
        }
    }
}
