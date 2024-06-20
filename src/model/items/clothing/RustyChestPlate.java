package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import model.items.StartingItem;
import util.MyPair;
import view.sprites.Sprite;

import java.util.List;

public class RustyChestPlate extends Clothing implements StartingItem {

    private BreastPlate breastPlate = new BreastPlate();

    public RustyChestPlate() {
        super("Rusty Chest Plate", 20, 4, true);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Acrobatics, -1),
                new MyPair<>(Skill.SeekInfo, -1),
                new MyPair<>(Skill.Sneak, -1));
    }

    @Override
    protected Sprite getSprite() {
        return breastPlate.getSprite();
    }

    @Override
    public int getWeight() {
        return breastPlate.getWeight();
    }

    @Override
    public Item copy() {
        return new RustyChestPlate();
    }
}
