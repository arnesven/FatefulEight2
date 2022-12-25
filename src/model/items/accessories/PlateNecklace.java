package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class PlateNecklace extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(3, 9, MyColors.CYAN, MyColors.BROWN);

    public PlateNecklace() {
        super("Plate Necklace", 16);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Persuade, 2));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new PlateNecklace();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
