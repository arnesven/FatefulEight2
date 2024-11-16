package model.items.imbuements;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.spells.PoisonGasSpell;
import model.states.CombatEvent;
import util.MyRandom;

public class PoisonImbuement extends WeaponImbuement {

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        PoisonGasSpell spell = new PoisonGasSpell();
        if (!target.isDead() && MyRandom.rollD10() > 8) {
            spell.addPoisonGasEffect(combatEvent, gameCharacter, target);
        }
    }

    @Override
    public String getText() {
        return "20% Chance to apply Poison Gas";
    }
}
