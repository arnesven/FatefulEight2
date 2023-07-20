package model.combat;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.Enemy;
import model.items.Scroll;
import model.items.UsableItem;
import model.items.potions.ThrowablePotion;
import model.items.spells.CombatSpell;
import model.states.CombatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemCombatAction extends CombatAction {
    private final Set<UsableItem> usableItems;
    private final Combatant target;

    public ItemCombatAction(Set<UsableItem> usableItems, Combatant target) {
        super("Item");
        this.usableItems = usableItems;
        this.target = target;
    }

    @Override
    public void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        // Unused
    }

    @Override
    public boolean hasInnerMenu() {
        return true;
    }

    @Override
    public List<CombatAction> getInnerActions(Model model) {
        List<CombatAction> res = new ArrayList<>();
        for (UsableItem item : usableItems) {
            if (item instanceof Scroll) {
                if (((Scroll) item).getSpell() instanceof CombatSpell) {
                    CombatSpell spell = ((CombatSpell) ((Scroll) item).getSpell());
                    if (spell.canBeCastOn(model, target)) {
                        res.add(new ScrollCombatAction((Scroll)item));
                    }
                }
            } else if (target instanceof GameCharacter && item.canBeUsedOn(model, (GameCharacter) target)) {
                res.add(new CombatAction(item.getName()) {
                    @Override
                    public void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                        GameCharacter character = (GameCharacter)target;
                        String message = item.useYourself(model, character);
                        combat.println(message);
                        model.getParty().getInventory().remove(item);
                        character.addToAttitude(performer, 2);
                    }
                });
            } else if (target instanceof Enemy && item instanceof ThrowablePotion) {
                res.add(new CombatAction(item.getName()) {
                    @Override
                    public void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                        ThrowablePotion tp = (ThrowablePotion)item;
                        tp.throwYourself(model, combat, performer, target);
                        model.getParty().getInventory().remove(item);
                    }
                });
            }
        }
        return res;
    }
}
