package model.items.spells;

import model.Inventory;
import model.items.Item;
import view.MyColors;

public abstract class Spell extends Item {
    private final MyColors color;
    private final int difficulty;
    private final int hpCost;

    public Spell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost);
        this.color = color;
        this.difficulty = difficulty;
        this.hpCost = hpCost;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    public MyColors getColor() {
        return color;
    }
}
