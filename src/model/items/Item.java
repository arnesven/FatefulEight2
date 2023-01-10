package model.items;

import model.Inventory;
import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.enemies.Enemy;
import model.states.CombatEvent;
import util.MyPair;
import view.ScreenHandler;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Item implements Serializable, Comparable<Item> {

    public static final Sprite EMPTY_ITEM_SPRITE = new ItemSprite(0xF, 0);
    private final String name;
    private int cost;

    public Item(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        screenHandler.clearSpace(col, col+4, row, row+4);
        screenHandler.put(col, row, getSprite());
    }

    protected abstract Sprite getSprite();

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
}
