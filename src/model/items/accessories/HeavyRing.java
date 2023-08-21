package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class HeavyRing extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(8, 9, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY);

    public HeavyRing() {
        super("Heavy Ring", 10);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Axes, 1), new MyPair<>(Skill.Polearms, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new HeavyRing();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
