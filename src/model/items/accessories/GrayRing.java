package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class GrayRing extends JewelryItem {
    private static final Sprite SPRITE =  new ItemSprite(7, 9, MyColors.GRAY, MyColors.DARK_GRAY);

    public GrayRing() {
        super("Gray Ring", 8);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Blades, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new GrayRing();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
