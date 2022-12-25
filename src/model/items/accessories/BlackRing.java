package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class BlackRing extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(11, 7, MyColors.GOLD, MyColors.BROWN, MyColors.WHITE);

    public BlackRing() {
        super("Black Ring", 14);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.MagicBlack, 1), new MyPair<>(Skill.Sneak, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BlackRing();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
