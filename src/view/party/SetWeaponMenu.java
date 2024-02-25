package view.party;

import model.Model;
import model.characters.GameCharacter;
import model.items.Equipment;
import model.items.Item;
import model.items.weapons.Weapon;
import view.SimpleMessageView;

import java.util.Collection;

class SetWeaponMenu extends SetEquipmentMenu {

    public SetWeaponMenu(PartyView view, GameCharacter gc, int x, int y) {
        super(view, gc, x, y);
    }

    @Override
    protected Collection<? extends Item> getSpecificInventory(Model model) {
        return model.getParty().getInventory().getWeapons();
    }

    @Override
    protected boolean doAction(Model model, Item item, GameCharacter person) {
        String errorString = Equipment.canEquip(item, person);
        if (!errorString.equals("")) {
            setInnerMenu(new SimpleMessageView(this, errorString), model);
            return false;
        }
        person.equipWeaponFromInventory((Weapon) item);
        return true;
    }

    @Override
    protected void doUnequipAction(Model model, GameCharacter person) {
        person.unequipWeapon();
    }
}
