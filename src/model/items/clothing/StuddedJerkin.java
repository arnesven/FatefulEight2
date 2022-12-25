package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class StuddedJerkin extends Clothing {
    private static final Sprite SPRITE = new ItemSprite(1, 2, MyColors.DARK_GRAY, MyColors.LIGHT_GRAY);

    public StuddedJerkin() {
        super("Studded Jerkin", 22, 2, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new StuddedJerkin();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.SeekInfo, 1));
    }
}
