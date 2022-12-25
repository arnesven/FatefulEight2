package model.items.clothing;

import model.Inventory;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.ArmorItem;
import model.items.EquipableItem;
import model.items.Item;
import util.MyPair;

public abstract class Clothing extends EquipableItem implements ArmorItem {

    private final int ap;
    private final boolean heavy;

    public Clothing(String name, int cost, int ap, boolean heavy) {
        super(name, cost);
        this.ap = ap;
        this.heavy = heavy;
    }

    public int getAP() {
        return ap;
    }

    public boolean isHeavy() {
        return heavy;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    @Override
    public String getShoppingDetails() {
        StringBuilder result = new StringBuilder();
        result.append(getSkillBonusesAsString());
        return ", Armor " + getAP() + " " + (isHeavy()?"HEAVY":"LIGHT") + result.toString();
    }

    @Override
    public void equipYourself(GameCharacter gc) {
        gc.equipClothingFromInventory(this);
    }
}
