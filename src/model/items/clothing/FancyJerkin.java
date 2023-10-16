package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class FancyJerkin extends Clothing {
    private static final Sprite SPRITE = new ItemSprite(11, 2, MyColors.DARK_GRAY, MyColors.BEIGE);

    public FancyJerkin() {
        super("Fancy Jerkin", 18, 0, false);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Persuade, 1), new MyPair<>(Skill.SeekInfo, 1),
                new MyPair<>(Skill.Entertain, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 1000;
    }

    @Override
    public Item copy() {
        return new FancyJerkin();
    }
}
