package model;

import model.items.*;
import model.items.accessories.*;
import model.items.clothing.Clothing;
import model.items.clothing.DragonArmor;
import model.items.clothing.WarmCape;
import model.items.potions.Potion;
import model.items.spells.HarmonizeSpell;
import model.items.spells.Spell;
import model.items.weapons.*;

import java.io.Serializable;
import java.util.*;

public class Inventory implements Serializable {
    private List<Weapon> weapons = new ArrayList<>();
    private List<Clothing> clothing = new ArrayList<>();
    private List<Accessory> accessories = new ArrayList<>();
    private List<Spell> spells = new ArrayList<>();
    private List<Potion> potions = new ArrayList<>();
    private int food = 10;

    public Inventory() { }

    public void add(Weapon weapon) {
        weapons.add(weapon);
    }

    public void add(Clothing clothing) {
        this.clothing.add(clothing);
    }

    public void add(Accessory accessory) {
        accessories.add(accessory);
    }

    public void add(Spell spell) { spells.add(spell); }

    public void add(Potion potion) { potions.add(potion); }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public List<Clothing> getClothing() {
        return clothing;
    }

    public List<Potion> getPotions() { return potions; }

    public void remove(Weapon weapon) {
        this.weapons.remove(weapon);
    }

    public void remove(Clothing clothing) {
        this.clothing.remove(clothing);
    }

    public void remove(Accessory accessory) {
        this.accessories.remove(accessory);
    }

    public List<Accessory> getAccessories() {
        return accessories;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        for (Collection<? extends Item> itemSet : getItemSets()) {
            items.addAll(itemSet);
        }
        return items;
    }

    public void addItem(Item it) {
        it.addYourself(this);
    }


    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int noOfsellableItems() {
        return weapons.size() + clothing.size() + accessories.size() + potions.size();
    }

    public List<Spell> getSpells() {
        List<Spell> sp = new ArrayList<>();
        sp.addAll(spells);
        return sp;
    }

    public void remove(Item it) {
        for (Collection<? extends Item> itemSet : getItemSets()) {
            if (itemSet.contains(it)) {
                itemSet.remove(it);
            }
        }
    }

    private Set<Collection<? extends Item>> getItemSets() {
        Set<Collection<? extends Item>> sets = new HashSet<>();
        sets.add(weapons);
        sets.add(clothing);
        sets.add(accessories);
        sets.add(spells);
        sets.add(potions);
        return sets;
    }
}
