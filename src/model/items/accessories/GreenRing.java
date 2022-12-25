package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class GreenRing extends JewelryItem {
    private static final Sprite SPRITE =  new ItemSprite(10, 7, MyColors.TAN, MyColors.DARK_GREEN);

    public GreenRing() {
        super("Green Ring", 14);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.MagicGreen, 1), new MyPair<>(Skill.Labor, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new GreenRing();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
