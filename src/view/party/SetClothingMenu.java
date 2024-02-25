package view.party;

import model.Model;
import model.characters.GameCharacter;
import model.items.Equipment;
import model.items.clothing.Clothing;
import model.items.Item;
import model.items.ArmorItem;
import view.SimpleMessageView;

import java.util.Collection;

public class SetClothingMenu extends SetEquipmentMenu {
    public SetClothingMenu(PartyView partyView, GameCharacter gc, int x, int y) {
        super(partyView, gc, x, y);
    }

    @Override
    protected void doUnequipAction(Model model, GameCharacter person) {
        person.unequipArmor();
    }

    @Override
    protected boolean doAction(Model model, Item item, GameCharacter person) {
        String errorString = Equipment.canEquip(item, person);
        if (!errorString.equals("")) {
            setInnerMenu(new SimpleMessageView(this, errorString), model);
            return false;
        }
        person.equipClothingFromInventory((Clothing) item);
        return true;
    }

    @Override
    protected Collection<? extends Item> getSpecificInventory(Model model) {
        return model.getParty().getInventory().getClothing();
    }
}
