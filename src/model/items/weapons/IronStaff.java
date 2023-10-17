package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import util.MyPair;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

import java.util.List;

public class IronStaff extends StaffWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(4, 12,
            MyColors.GRAY, MyColors.DARK_RED, MyColors.PEACH);

    public IronStaff() {
        super("Iron Staff", 32, new int[]{6, 9, 11});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new IronStaff();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.SpellCasting, 1));
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
