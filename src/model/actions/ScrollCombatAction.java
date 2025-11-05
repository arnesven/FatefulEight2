package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.Scroll;
import model.items.spells.CombatSpell;
import model.states.CombatEvent;

public class ScrollCombatAction extends SpellFinalCombatAction {
    private final Scroll scroll;

    public ScrollCombatAction(Scroll item) {
        super((CombatSpell) item.getSpell());
        this.scroll = item;
    }

    @Override
    public void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        super.doAction(model, combat, performer, target);
        model.getParty().removeFromInventory(scroll);
    }
}
