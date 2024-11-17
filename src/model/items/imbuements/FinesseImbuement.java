package model.items.imbuements;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.CombatEvent;

public class FinesseImbuement extends WeaponImbuement {
    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter,
                                 Combatant target, int damage, int critical) {

    }

    @Override
    public String getText() {
        return "+1 to Attacks";
    }

    @Override
    public int getAttackBonus() {
        return 1;
    }
}
