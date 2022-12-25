package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import util.MyPair;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class FullPlateArmor extends SuperHeavyArmorClothing {
    private static final Sprite SPRITE = new ItemSprite(9, 2);

    public FullPlateArmor() {
        super("Full Plate Armor", 50, 7);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new FullPlateArmor();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        List<MyPair<Skill, Integer>> list = super.getSkillBonuses();
        for (MyPair<Skill, Integer> p : list) {
            p.second--;
        }
        return list;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
