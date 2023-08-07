package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class Brigandine extends SuperHeavyArmorClothing {
    private static final Sprite SPRITE = new BrigandineSprite();

    public Brigandine() {
        super("Brigandine", 36, 6);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Item copy() {
        return new Brigandine();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        List<MyPair<Skill, Integer>> list = super.getSkillBonuses();
        for (MyPair<Skill, Integer> p : list) {
            p.second--;
        }
        return list;
    }

    private static class BrigandineSprite extends ItemSprite {
        public BrigandineSprite() {
            super(14, 3);
            setColor2(MyColors.BROWN);
            setColor3(MyColors.LIGHT_GRAY);
            setColor4(MyColors.ORANGE);
        }
    }
}
