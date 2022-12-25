package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class SkullRing extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(8, 7, MyColors.GRAY, MyColors.DARK_GRAY, MyColors.LIGHT_GRAY);

    public SkullRing() {
        super("Skull Ring", 8);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Security, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new SkullRing();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
