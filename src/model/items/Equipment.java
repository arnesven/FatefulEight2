package model.items;

import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.conditions.Condition;
import model.enemies.Enemy;
import model.items.accessories.Accessory;
import model.items.accessories.ShieldItem;
import model.items.clothing.Clothing;
import model.items.clothing.JustClothes;
import model.items.clothing.MytheriumArmor;
import model.items.weapons.UnarmedCombatWeapon;
import model.items.weapons.Weapon;
import model.states.CombatEvent;
import util.MyPair;
import view.ScreenHandler;
import view.sprites.Sprite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Equipment implements Serializable {
    private Weapon weapon;
    private Clothing clothing;
    private Accessory accessory;
    private static Sprite nothingSprite = Item.EMPTY_ITEM_SPRITE;

    public Equipment() {
        weapon = new UnarmedCombatWeapon();
        clothing = new JustClothes();
    }

    public Equipment(Weapon weapon) {
        this();
        this.weapon = weapon;
    }

    public Equipment(Weapon weapon, Clothing clothes, Accessory accessory) {
        this.weapon = weapon;
        this.clothing = clothes;
        this.accessory = accessory;
    }

    public static String canEquip(Item item, GameCharacter person) {
        if (item instanceof Clothing && !person.canChangeClothing()) {
            return person.getFirstName() + " cannot unequip clothing.";
        }

        if (item instanceof Accessory && !person.canChangeAccessory()) {
            return person.getFirstName() + " cannot unequip accessory.";
        }

        if (item instanceof ArmorItem && !person.getCharClass().canUseHeavyArmor() && ((ArmorItem)item).isHeavy()) {
            return person.getFirstName() + " cannot wear Heavy Armor.";
        }
        if (item instanceof Weapon && ((Weapon) item).isTwoHanded()
                && person.getEquipment().getAccessory() != null
                && person.getEquipment().getAccessory().isOffHandItem()) {
            return "Cannot equip a two-handed weapon while an off-hand item is equipped.";
        }
        if (item instanceof ShieldItem && person.getEquipment().getWeapon().isTwoHanded()) {
            return "Cannot equip an off-hand item while a two-handed weapon is equipped.";
        }

        int weaponWeight = person.getEquipment().getWeapon().getWeight();
        int clothesWeight = person.getEquipment().getClothing().getWeight();
        int carryCap = person.getRace().getCarryingCapacity() * 1000;
        if (((item instanceof Weapon) &&
                (item.getWeight() + clothesWeight + weightOfAccessory(person) > carryCap)) ||
            ((item instanceof Clothing) &&
                (item.getWeight() + weaponWeight + weightOfAccessory(person) > carryCap)) ||
            ((item instanceof Accessory) &&
                (item.getWeight() + weaponWeight + clothesWeight > carryCap))) {
            return "The total weight of equipped items would exceed the character's carrying capacity.";
        }
        return "";
    }

    private static int weightOfAccessory(GameCharacter person) {
        if (person.getEquipment().getAccessory() == null) {
            return 0;
        }
        return person.getEquipment().getAccessory().getWeight();
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        drawWeapon(screenHandler, col+8, row+6);
        drawClothing(screenHandler, col+13, row+6);
        drawAccessory(screenHandler, col+18, row+6);
    }

    public void drawAccessory(ScreenHandler screenHandler, int x, int y) {
        if (accessory != null) {
            accessory.drawYourself(screenHandler, x, y);
        } else {
            screenHandler.clearSpace(x, x+4, y, y+4);
            screenHandler.put(x, y, nothingSprite);
        }
    }

    public void drawClothing(ScreenHandler screenHandler, int x, int y) {
        clothing.drawYourself(screenHandler, x, y);
    }

    public void drawWeapon(ScreenHandler screenHandler, int x, int y) {
        weapon.drawYourself(screenHandler, x, y);
    }

    public int getTotalAP() {
        if (accessory != null) {
            return clothing.getAP() + accessory.getAP();
        }
        return clothing.getAP();
    }

    public int getSpeedModifiers() {
        int modifier = clothing.getSpeedModifier();
        modifier += weapon.getSpeedModifier();
        if (accessory != null) {
            modifier += accessory.getSpeedModifier();
        }
        return modifier;
    }

    public Clothing getClothing() {
        return clothing;
    }

    public Accessory getAccessory() {
        return accessory;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void setClothing(Clothing clothing) {
        this.clothing = clothing;
    }

    public void setAccessory(Accessory accessory) {
        this.accessory = accessory;
    }

    public void transferToParty(Inventory inventory) {
        if (!(weapon instanceof UnarmedCombatWeapon)) {
            inventory.add(weapon);
        }
        if (!(clothing instanceof JustClothes)) {
            inventory.add(clothing);
        }
        if (accessory != null) {
            inventory.add(accessory);
        }
    }

    public void wielderWasAttackedBy(Enemy enemy, CombatEvent combatEvent) {
        weapon.wielderWasAttackedBy(enemy, combatEvent);
        clothing.wielderWasAttackedBy(enemy, combatEvent);
        if (accessory != null) {
            accessory.wielderWasAttackedBy(enemy, combatEvent);
        }
    }

    public int getBonusForSkill(Skill skill) {
        List<MyPair<Skill, Integer>> bonuses = new ArrayList<>();
        bonuses.addAll(weapon.getSkillBonuses());
        bonuses.addAll(clothing.getSkillBonuses());
        if (accessory != null) {
            bonuses.addAll(accessory.getSkillBonuses());
        }
        int result = 0;
        for (MyPair<Skill, Integer> pair : bonuses) {
            if (pair.first.areEqual(skill)) {
                result += pair.second;
            }
        }
        return result;
    }

    public int getHealthBonus() {
        if (accessory != null) {
            return accessory.getHealthBonus();
        }
        return 0;
    }

    public boolean anyHeavy() {
        return clothing.isHeavy() || (accessory != null && accessory.isHeavy());
    }

    public int getTotalWeight() {
        int weaponWeight = weapon == null ? 0 : weapon.getWeight();
        int clothingWeight = clothing == null ? 0 : clothing.getWeight();
        int accessoryWeight = accessory == null ? 0 : accessory.getWeight();
        return weaponWeight + clothingWeight + accessoryWeight;
    }

    public boolean grantsConditionImmunity(Condition cond) {
        return clothing.grantsConditionImmunity(cond) || (accessory != null && accessory.grantsConditionImmunity(cond));
    }

    public boolean applyArmorToMagicAttacks() {
        return clothing instanceof MytheriumArmor;
    }

    public int getTotalCost() {
        return weapon.getCost() + clothing.getCost() + (accessory != null ? accessory.getCost() : 0);
    }

    public boolean contains(Item it) {
        return weapon == it || clothing == it || accessory == it;
    }
}
