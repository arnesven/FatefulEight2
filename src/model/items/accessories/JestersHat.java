package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class JestersHat extends HeadGearItem {
    private static final Sprite SPRITE = new ItemSprite(10, 10, MyColors.DARK_PURPLE, MyColors.DARK_RED);

    public JestersHat() {
        super("Jester's Hat", 10);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Entertain, 2));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new JestersHat();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
