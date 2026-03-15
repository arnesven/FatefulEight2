package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import util.MyPair;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

import java.util.List;

public class IronStaff extends StaffWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(4, 12,
            MyColors.GRAY, MyColors.DARK_RED, MyColors.PEACH);

    private static final AvatarItemSprite STAFF_SPRITES =
            new AvatarItemSprite(0x23, MyColors.DARK_RED, MyColors.DARK_RED, MyColors.DARK_RED, MyColors.TRANSPARENT);

    public IronStaff() {
        super("Iron Staff", 28, new int[]{6, 9, 11});
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

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return STAFF_SPRITES;
    }
}
