package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class Crown extends HeadGearItem {
    private Sprite sprite;

    public Crown() {
        super("Crown", 24);
        if (MyRandom.randInt(2) == 0) {
            sprite = new ItemSprite(12, 10, MyColors.GOLD, MyColors.DARK_RED);
        } else {
            sprite = new ItemSprite(12, 10, MyColors.LIGHT_GRAY, MyColors.RED);
        }
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Leadership, 1));
    }

    @Override
    public Item copy() {
        return new Crown();
    }

    @Override
    public int getAP() {
        return 1;
    }
}
