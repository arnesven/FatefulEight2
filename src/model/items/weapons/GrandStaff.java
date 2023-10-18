package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import util.MyPair;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

import java.util.List;

public class GrandStaff extends StaffWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(5, 12,
            MyColors.PINK, MyColors.GRAY, MyColors.LIGHT_GREEN);

    public GrandStaff() {
        super("Grand Staff", 34, new int[]{6, 9, 9, 11});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.SpellCasting, 1));
    }

    @Override
    public Item copy() {
        return new GrandStaff();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
