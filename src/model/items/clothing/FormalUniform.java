package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class FormalUniform extends Clothing {

    private static final Sprite SPRITE = new ItemSprite(0, 8,
            MyColors.DARK_BLUE, MyColors.TAN, MyColors.DARK_RED);

    public FormalUniform() {
        super("Formal Uniform", 20, 1, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Leadership, 1), new MyPair<>(Skill.Mercantile, 1));
    }

    @Override
    public int getWeight() {
        return 1500;
    }

    @Override
    public Item copy() {
        return new FormalUniform();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
