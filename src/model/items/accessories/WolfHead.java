package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class WolfHead extends HeadGearItem {
    private static final Sprite SPRITE = new ItemSprite(14, 10, MyColors.DARK_GRAY, MyColors.LIGHT_GRAY);

    public WolfHead() {
        super("Wolf Head", 20);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.MagicGreen, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new WolfHead();
    }

    @Override
    public int getAP() {
        return 1;
    }
}
