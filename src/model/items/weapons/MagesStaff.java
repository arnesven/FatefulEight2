package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class MagesStaff extends StaffWeapon {
    private static final AvatarItemSprite STAFF_SPRITES =
            new AvatarItemSprite(0x23, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.LIGHT_GRAY);

    private static final Sprite SPRITE = new TwoHandedItemSprite(3, 1);

    public MagesStaff() {
        super("Mage's Staff", 20, new int[]{6, 10});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new MagesStaff();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.SpellCasting, 1));
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return STAFF_SPRITES;
    }
}
