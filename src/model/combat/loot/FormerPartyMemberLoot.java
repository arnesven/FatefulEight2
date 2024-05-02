package model.combat.loot;

import model.Party;
import model.characters.GameCharacter;
import model.items.accessories.Accessory;
import model.items.clothing.Clothing;
import model.items.clothing.JustClothes;
import model.items.weapons.UnarmedCombatWeapon;
import model.items.weapons.Weapon;

public class FormerPartyMemberLoot extends CombatLoot {

    private final Weapon weapon;
    private final Clothing clothing;
    private final Accessory accessory;
    private final String basedOnName;

    public FormerPartyMemberLoot(GameCharacter basedOn) {
        this.weapon = basedOn.getEquipment().getWeapon();
        this.clothing = basedOn.getEquipment().getClothing();
        this.accessory = basedOn.getEquipment().getAccessory();
        this.basedOnName = basedOn.getFullName();
    }

    @Override
    public String getText() {
        return basedOnName + "'s items";
    }

    @Override
    protected void specificGiveYourself(Party party) {
        if (!(weapon instanceof UnarmedCombatWeapon)) {
            party.getInventory().add(weapon);
        }
        if (!(clothing instanceof JustClothes)) {
            party.getInventory().add(clothing);
        }
        if (accessory != null) {
            party.getInventory().add(accessory);
        }
    }
}
