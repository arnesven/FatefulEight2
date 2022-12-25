package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class EyeRing extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(9, 7, MyColors.GRAY, MyColors.DARK_GRAY, MyColors.CYAN);

    public EyeRing() {
        super("Eye Ring", 16);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Search, 2));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new EyeRing();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
