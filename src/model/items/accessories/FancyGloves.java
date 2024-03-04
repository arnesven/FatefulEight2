package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class FancyGloves extends GlovesItem {
    private Sprite sprite;

    public FancyGloves() {
        super("Fancy Gloves", 14);
        int rand = MyRandom.randInt(3);
        if (rand == 0) {
            sprite = new ItemSprite(3, 0xA, MyColors.DARK_RED, MyColors.DARK_BROWN, MyColors.GOLD);
        } else if (rand == 1) {
            sprite = new ItemSprite(3, 0xA, MyColors.BLUE, MyColors.DARK_BLUE, MyColors.GOLD);
        } else {
            sprite = new ItemSprite(3, 0xA, MyColors.TAN, MyColors.DARK_GREEN, MyColors.LIGHT_GRAY);
        }
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.SeekInfo, 1), new MyPair<>(Skill.Entertain, 1));
    }

    @Override
    public Item copy() {
        return new FancyGloves();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
