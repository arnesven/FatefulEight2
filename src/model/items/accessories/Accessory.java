package model.items.accessories;

import model.Inventory;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.ArmorItem;
import model.items.EquipableItem;
import model.items.Item;
import model.items.spells.Spell;
import util.MyPair;
import util.MyStrings;

public abstract class Accessory extends EquipableItem implements ArmorItem {
    public Accessory(String name, int cost) {
        super(name, cost);
    }

    public abstract int getAP();

    @Override
    public boolean isHeavy() { return false; }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    @Override
    public final String getShoppingDetails() {
        StringBuilder result = new StringBuilder();
        if (getAP() != 0) {
            result.append(", Armor " + getAP() + " " + (isHeavy() ? "HEAVY" : "LIGHT"));
        }
        if (getSpeedModifier() != 0) {
            result.append(", Speed " + MyStrings.withPlus(getSpeedModifier()));
        }
        if (getHealthBonus() != 0) {
            result.append(", Health " + MyStrings.withPlus(getHealthBonus()));
        }
        if (getSPBonus() != 0) {
            result.append(", Stamina " + MyStrings.withPlus(getSPBonus()));
        }
        result.append(getSkillBonusesAsString());
        if (!getExtraText().equals("")) {
            result.append(", " + getExtraText());
        }
        return result.toString();
    }

    public String getExtraText() {
        return "";
    }

    public int[] getDamageTable() { return null; }

    public int getHealthBonus() { return 0; }

    public int getSPBonus() { return 0; }

    public int getSpellDiscount(Spell sp) { return 0; }

    @Override
    public void equipYourself(GameCharacter gc) {
        gc.equipAccessoryFromInventory(this);
    }
}
