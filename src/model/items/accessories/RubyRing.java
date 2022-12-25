package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class RubyRing extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(6, 9, MyColors.GOLD, MyColors.RED, MyColors.LIGHT_PINK);

    public RubyRing() {
        super("Ruby Ring", 14);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Logic, 1), new MyPair<>(Skill.Perception, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new RubyRing();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
