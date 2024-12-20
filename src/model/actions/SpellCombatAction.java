package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.abilities.CombatAction;
import model.items.spells.CombatSpell;
import model.states.CombatEvent;

import java.util.*;

public class SpellCombatAction extends BasicCombatAction {
    private final List<CombatSpell> combatSpells;
    private final Combatant target;

    public SpellCombatAction(List<CombatSpell> combatSpells, Combatant target) {
        super("Spell", false, false);
        this.combatSpells = combatSpells;
        this.target = target;
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        // unused
    }

    @Override
    public boolean hasInnerMenu() {
        return true;
    }

    @Override
    public List<CombatAction> getInnerActions(Model model) {
        List<CombatAction> res = new ArrayList<>();
        Set<String> added = new HashSet<>();
        for (CombatSpell spell : combatSpells) {
            if (spell.canBeCastOn(model, target) && !added.contains(spell.getName())) {
                res.add(new SpellFinalCombatAction(spell));
                added.add(spell.getName());
            }
        }
        return res;
    }

}
