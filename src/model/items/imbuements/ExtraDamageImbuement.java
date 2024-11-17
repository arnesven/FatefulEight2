package model.items.imbuements;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.CombatEvent;

public class ExtraDamageImbuement extends WeaponImbuement {

    @Override
    public int[] makeDamageTable(int[] damageTable) {
        int[] burningTable = new int[damageTable.length+1];
        burningTable[0] = damageTable[0];
        for (int i = 0; i < damageTable.length; ++i) {
            burningTable[i+1] = damageTable[i];
        }
        return burningTable;
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter,
                                 Combatant target, int damage, int critical) {

    }

    @Override
    public String getText() {
        return "Increased damage";
    }
}
