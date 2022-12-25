package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class Tiara extends HeadGearItem {
    private static final Sprite SPRITE = new ItemSprite(15, 9, MyColors.LIGHT_GRAY, MyColors.LIGHT_BLUE);

    public Tiara() {
        super("Tiara", 18);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Leadership, 1), new MyPair<>(Skill.MagicBlue, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Tiara();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
