package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class Circlet extends HeadGearItem {
    private static final Sprite SPRITE = new ItemSprite(15, 10, MyColors.LIGHT_GRAY, MyColors.GOLD);

    public Circlet() {
        super("Circlet", 18);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Leadership, 1), new MyPair<>(Skill.MagicWhite, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Circlet();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
