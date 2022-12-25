package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import util.MyPair;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class OutlawArmor extends Clothing {
    private static final Sprite SPRITE = new ItemSprite(2, 2);

    public OutlawArmor() {
        super("Outlaw Armor", 18, 2, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new OutlawArmor();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.SeekInfo, -1));
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
