package model.combat.loot;


import model.Party;
import model.items.Item;
import model.items.accessories.CrudeShield;
import model.items.designs.CraftingDesign;
import model.items.weapons.*;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class DungeonInmateLoot extends CombatLoot {

    private static final List<Item> DROPS = List.of(
            new Shiv(), new Truncheon(), new PointyStick(),
            new Sling(), new MakeshiftCrossbow(), new ImprovisedWand()
    );

    private final ArrayList<Item> items;
    private int materials = 0;

    public DungeonInmateLoot() {
        this.items = new ArrayList<>();
        items.add(MyRandom.sample(DROPS).copy());
        int roll = MyRandom.rollD6();
        if (roll < 4) {
            this.materials = MyRandom.randInt(3, 10);
        } else if (roll < 6) {
            items.add(new CraftingDesign(MyRandom.sample(DROPS).copy()));
        } else {
            items.add(new CrudeShield());
        }
    }

    @Override
    public int getMaterials() {
        return materials;
    }

    @Override
    protected void specificGiveYourself(Party party) {
        for (Item it : items) {
            party.getInventory().addItem(it);
        }
    }

    @Override
    public String getText() {
        StringBuilder bldr = new StringBuilder();
        if (items.size() > 0) {
            bldr.append(items.get(0).getName());
        }
        for (int i = 1; i < items.size(); ++i) {
            bldr.append("\n").append(items.get(i).getName());
        }
        return bldr.toString();
    }
}
