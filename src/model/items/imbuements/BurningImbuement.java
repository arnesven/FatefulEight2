package model.items.imbuements;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.BurningCondition;
import model.states.CombatEvent;
import util.MyRandom;

public class BurningImbuement extends WeaponImbuement {
    @Override
    public String getText() {
        return "20% Chance to apply Burn";
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        if (!target.isDead() && MyRandom.rollD10() > 8 && !target.hasCondition(BurningCondition.class)) {
            combatEvent.println(target.getName() + " starts burning!");
            target.addCondition(new BurningCondition(gameCharacter));
        }
    }
}
