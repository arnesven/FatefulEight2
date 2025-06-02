package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class SilverPendant extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(0, 9, MyColors.LIGHT_GRAY, MyColors.RED, MyColors.ORANGE);

    public SilverPendant() {
        super("Silver Pendant", 8);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Mercantile, 1));
    }

    @Override
    public Item copy() {
        return new SilverPendant();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
