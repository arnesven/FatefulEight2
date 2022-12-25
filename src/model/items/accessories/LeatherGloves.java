package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class LeatherGloves extends GlovesItem {
    private static final Sprite SPRITE = new ItemSprite(2, 0xA, MyColors.BROWN, MyColors.DARK_BROWN);

    public LeatherGloves() {
        super("Leather Gloves", 8);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Survival, 1), new MyPair<>(Skill.Labor, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new LeatherGloves();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
