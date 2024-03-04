package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class DeftGloves extends GlovesItem {
    private static final Sprite SPRITE = new ItemSprite(1, 0xA, MyColors.GRAY, MyColors.DARK_GRAY);

    public DeftGloves() {
        super("Deft Gloves", 16);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Security, 1), new MyPair<>(Skill.Persuade, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new DeftGloves();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
