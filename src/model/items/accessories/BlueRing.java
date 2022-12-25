package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class BlueRing extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(9, 9, MyColors.GOLD, MyColors.BEIGE, MyColors.LIGHT_BLUE);

    public BlueRing() {
        super("Blue Ring", 14);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.MagicBlue, 1), new MyPair<>(Skill.Entertain, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BlueRing();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
