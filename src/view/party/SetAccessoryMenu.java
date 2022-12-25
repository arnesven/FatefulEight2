package view.party;

import model.Model;
import model.characters.GameCharacter;
import model.items.Equipment;
import model.items.accessories.Accessory;
import model.items.ArmorItem;
import model.items.Item;
import view.SimpleMessageView;

import java.util.Collection;

public class SetAccessoryMenu extends SetEquipmentMenu {
    public SetAccessoryMenu(PartyView partyView, GameCharacter gc, int x, int y) {
        super(partyView, gc, x, y);
    }

    @Override
    protected void doUnequipAction(Model model, GameCharacter person) {
        person.unequipAccessory();
    }

    @Override
    protected boolean doAction(Model model, Item item, GameCharacter person) {
        String errorString = Equipment.canEquip(model, item, person);
        if (!errorString.equals("")) {
            setInnerMenu(new SimpleMessageView(this, errorString), model);
            return false;
        }
        person.equipAccessoryFromInventory((Accessory)item);
        return true;
    }

    @Override
    protected Collection<? extends Item> getSpecificInventory(Model model) {
        return model.getParty().getInventory().getAccessories();
    }
}
