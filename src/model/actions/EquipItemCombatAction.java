package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.EquipableItem;
import model.items.Equipment;
import model.items.Item;
import model.items.weapons.Weapon;
import model.states.CombatEvent;
import sound.SoundEffects;

import java.util.ArrayList;
import java.util.List;

public class EquipItemCombatAction extends BasicCombatAction {
    private final List<Weapon> items;

    public EquipItemCombatAction(Model model) {
        super("Equip", false, false);
        this.items = new ArrayList<>();
        items.addAll(model.getParty().getInventory().getWeapons());
    }

    public boolean isValid() {
        return !items.isEmpty();
    }

    @Override
    public boolean hasInnerMenu() {
        return true;
    }

    @Override
    public List<CombatAction> getInnerActions(Model model) {
        List<CombatAction> result = new ArrayList<>();
        for (Weapon w : items) {
            result.add(new BasicCombatAction(w.getName(), false, false) {
                private boolean anotherAction = false;

                @Override
                protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                    String message = Equipment.canEquip(w, performer);
                    if (message.equals("")) {
                        ((EquipableItem) w).equipYourself(performer);
                        SoundEffects.playSound(w.getSound());
                        combat.println(performer.getFirstName() + " equipped " + w.getName() + ".");
                    } else {
                        combat.println(message);
                        anotherAction = true;
                    }
                }

                @Override
                public boolean takeAnotherAction() {
                    return anotherAction;
                }
            });
        }
        return result;
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        // unused
    }
}
