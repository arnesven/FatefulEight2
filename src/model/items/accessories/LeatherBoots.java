package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class LeatherBoots extends ShoesItem {
    private static final Sprite SPRITE = new ItemSprite(7, 10,MyColors.DARK_BROWN, MyColors.TAN, MyColors.BROWN);

    public LeatherBoots() {
        super("Leather Boots", 20);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Survival, 1));
    }

    @Override
    public Item copy() {
        return new LeatherBoots();
    }

    @Override
    public int getAP() {
        return 1;
    }
}
