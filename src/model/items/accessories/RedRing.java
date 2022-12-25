package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class RedRing extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(9, 9, MyColors.GOLD, MyColors.BEIGE, MyColors.RED);

    public RedRing() {
        super("Red Ring", 14);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.MagicRed, 1), new MyPair<>(Skill.BluntWeapons, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new RedRing();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
