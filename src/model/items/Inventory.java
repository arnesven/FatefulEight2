package model.items;

import model.items.accessories.*;
import model.items.clothing.Clothing;
import model.items.designs.CraftingDesign;
import model.items.parcels.Parcel;
import model.items.potions.Potion;
import model.items.special.PearlItem;
import model.items.spells.AuxiliarySpell;
import model.items.spells.CombatSpell;
import model.items.spells.Spell;
import model.items.weapons.*;
import model.states.fishing.Fish;

import java.io.Serializable;
import java.util.*;

public class Inventory implements Serializable {
    public static final int WEIGHT_OF_FOOD = 500;
    public static final int WEIGHT_OF_MATERIALS = 1000;
    public static final int WEIGHT_OF_INGREDIENTS = 100;
    public static final int WEIGHT_OF_LOCKPICKS = 10;
    private static final int STARTING_GOLD = 20;

    private final List<Weapon> weapons = new ArrayList<>();
    private final List<Clothing> clothing = new ArrayList<>();
    private final List<Accessory> accessories = new ArrayList<>();
    private final List<Spell> spells = new ArrayList<>();
    private final List<Potion> potions = new ArrayList<>();
    private final List<CraftingDesign> designs = new ArrayList<>();
    private final List<PotionRecipe> recipes = new ArrayList<>();
    private final List<Scroll> scrolls = new ArrayList<>();
    private final List<Item> specialItems = new ArrayList<>();
    private final List<Item> fishes = new ArrayList<>();
    private final List<Parcel> parcels = new ArrayList<>();
    private final List<BookItem> books = new ArrayList<>();
    private int gold = STARTING_GOLD;
    private int obols = 0;
    private int food = 10;
    private int ingredients = 0;
    private int materials = 0;
    private int lockpicks = 0;

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

    public void add(CraftingDesign design) { designs.add(design); }

    public void add(PotionRecipe recipe) { recipes.add(recipe); }

    public void add(Scroll scroll) { scrolls.add(scroll); }

    public void addSpecialItem(PearlItem item) {
        specialItems.add(item);
    }

    public void add(Fish fish) { fishes.add(fish); }

    public void add(Parcel parcel) { parcels.add(parcel); }

    public void add(BookItem book) { books.add(book); }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public List<Clothing> getClothing() {
        return clothing;
    }

    public List<Potion> getPotions() { return potions; }

    public List<PotionRecipe> getRecipes() { return recipes; }

    public List<CraftingDesign> getCraftingDesigns() { return designs; }

    public List<Scroll> getScrolls() { return scrolls; }

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

    public int getGold() { return gold; }

    public void setGold(int amount) { gold = amount; }

    public int getObols() { return obols; }

    public void setObols(int amount) { obols = amount; }

    public int noOfsellableItems() {
        return getAllItems().size();
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
        sets.add(designs);
        sets.add(recipes);
        sets.add(scrolls);
        sets.add(fishes);
        sets.add(specialItems);
        sets.add(parcels);
        sets.add(books);
        return sets;
    }

    public List<CombatSpell> getCombatSpells() {
        List<CombatSpell> result = new ArrayList<>();
        for (Spell sp : spells) {
            if (sp instanceof CombatSpell) {
                result.add((CombatSpell) sp);
            }  else if (sp instanceof AuxiliarySpell && ((AuxiliarySpell)sp).canBeCastInCombat()) {
                result.add(((AuxiliarySpell)sp).getCombatSpell());
            }
        }
        return result;
    }

    public int getIngredients() {
        return ingredients;
    }

    public int getMaterials() {
        return materials;
    }

    public void addToMaterials(int materials) {
        this.materials += materials;
    }

    public void addToIngredients(int ingredients) {
        this.ingredients += ingredients;
    }

    public void addToLockpicks(int i) { this.lockpicks += i; }

    public List<Scroll> getCombatScrolls() {
        List<Scroll> result = new ArrayList<>();
        for (Scroll scroll : scrolls) {
            if (scroll.getSpell() instanceof CombatSpell) {
                result.add(scroll);
            }
        }
        return result;
    }

    public List<Item> getPearls() {
        return specialItems;
    }

    public void removePearl(PearlItem pearl) {
        specialItems.remove(pearl);
    }

    public int getTotalWeight() {
        int weightInGrams = 0;
        for (Item it : getAllItems()) {
            weightInGrams += it.getWeight();
        }
        weightInGrams += (food * WEIGHT_OF_FOOD);
        weightInGrams += (materials * WEIGHT_OF_MATERIALS);
        weightInGrams += (ingredients * WEIGHT_OF_INGREDIENTS);
        weightInGrams += (lockpicks * WEIGHT_OF_LOCKPICKS);
        return weightInGrams;
    }

    public int getLockpicks() {
        return lockpicks;
    }

    public void addFish(Fish fish) {
        fishes.add(fish);
    }

    public List<Item> getFish() {
        return fishes;
    }

    public List<Parcel> getParcels() {
        return parcels;
    }

    public List<BookItem> getBooks() {
        return books;
    }
}
