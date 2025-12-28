package model.items;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.conditions.Condition;
import model.enemies.Enemy;
import model.items.analysis.ItemAnalysis;
import model.items.spells.Spell;
import model.states.CombatEvent;
import model.states.ShopState;
import util.MyPair;
import util.MyStrings;
import view.GameView;
import view.InventoryView;
import view.ScreenHandler;
import view.party.SelectableListMenu;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Item implements Serializable, Comparable<Item> {

    public static final Sprite EMPTY_ITEM_SPRITE = new ItemSprite(0xF, 0);
    public static final String[] TIER_PREFIXES = new String[]{"Fine", "Superior", "Premium", "Exquisite"};
    public static final int MAX_TIER = TIER_PREFIXES.length;
    private final String name;
    private int cost;

    public Item(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    protected static String getHigherTierPrefix(int tier) {
        if (tier > MAX_TIER) {
            tier = MAX_TIER;
        }
        return TIER_PREFIXES[tier-1];
    }

    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        screenHandler.clearSpace(col, col+4, row, row+4);
        screenHandler.put(col, row, getSprite());
    }

    protected abstract Sprite getSprite();
    public abstract int getWeight();
    public abstract boolean isCraftable();
    public abstract boolean isSellable();

    public boolean isStackable() {
        return false;
    }

    public int getSpeedModifier() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public abstract void addYourself(Inventory inventory);

    public abstract String getShoppingDetails();

    public abstract Item copy();

    public void wielderWasAttackedBy(Enemy enemy, CombatEvent combatEvent) { }

    public List<MyPair<Skill, Integer>> getSkillBonuses() { return new ArrayList<>(); }

    protected String getSkillBonusesAsString() {
        StringBuilder result = new StringBuilder();
        for (MyPair<Skill, Integer> pair : getSkillBonuses()) {
            result.append(", " + pair.first.getName() + " " + (pair.second>0?"+":"") + pair.second);
        }
        return result.toString();
    }

    public Prevalence getPrevalence() {
        return Prevalence.uncommon;
    }

    @Override
    public int compareTo(Item t1) {
        String st1 = getClass().getSuperclass().getName();
        String st2 = t1.getClass().getSuperclass().getName();
        if (st2.compareTo(st1) == 0) {
            return getName().compareTo(t1.getName());
        }
        return st2.compareTo(st1);
    }

    public abstract String getSound();

    public boolean isAnalyzable() {
        return false;
    }

    public List<ItemAnalysis> getAnalyses(Model model) {
        return new ArrayList<>();
    }

    public boolean supportsHigherTier() {
        return false;
    }

    public Item makeHigherTierCopy(int tier) {
        throw new IllegalStateException("Higher Tier not implemented for this item: " + getName());
    }

    public Sprite getSpriteForHigherTier(int tier) {
        return getSprite();
    }

    public int getSpriteSize() {
        return 4;
    }

    public boolean canBeUsedFromMenu() {
        return false;
    }

    public boolean hasDualUseInMenu() {
        return false;
    }

    public String getDualUseLabel() {
        return null;
    }

    public SelectableListMenu getDualUseMenu(GameView innerView, int x, int y) {
        return null;
    }

    public boolean opensViewFromInventoryMenu() {
        return false;
    }

    public GameView getViewFromInventoryMenu(Model model, InventoryView inventoryView, GameCharacter user) {
        return null;
    }

    protected static String makeAbbreviation(String str) {
        StringBuilder bldr = new StringBuilder();
        bldr.append(str.charAt(0));
        bldr.append(str.charAt(1));
        bldr.append(str.charAt(str.length()-1));
        return bldr.toString().toUpperCase();
    }

    public int getSellValue(int mercantileRank) {
        return (int)Math.round(ShopState.getSellRateForMercantile(mercantileRank) * getCost());
    }

    public boolean grantsConditionImmunity(Condition cond) {
        return false;
    }

    public static String getNameWithArticle(Item it) {
        String extra = "";
        if (it instanceof Spell) {
            extra = "a spell, ";
        } else {
            extra = MyStrings.aOrAn(it.getName()) + " ";
        }
        return  extra + it.getName();
    }
}
