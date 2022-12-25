package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class GoodShoes extends ShoesItem {
    private static final Sprite SPRITE = new ItemSprite(0, 0xA, MyColors.BROWN, MyColors.DARK_BROWN);

    public GoodShoes() {
        super("Good Shoes", 4);
    }

    @Override
    public int getAP() {
        return 0;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new GoodShoes();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Survival, 1));
    }
}
