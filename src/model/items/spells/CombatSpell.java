package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.CombatEvent;
import view.MyColors;

public abstract class CombatSpell extends Spell {
    public CombatSpell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost, color, difficulty, hpCost);
    }

    public abstract boolean canBeCastOn(Model model, Combatant target);

    public abstract void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target);
}
