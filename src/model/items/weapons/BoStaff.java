package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class BoStaff extends BluntWeapon {

    private static final AvatarItemSprite AVATAR_SPRITE =
            new FixedAvatarItemSprite( 0x90, MyColors.BROWN, MyColors.BROWN, MyColors.TRANSPARENT, MyColors.TRANSPARENT);

    private static final Sprite SPRITE = new TwoHandedItemSprite(0, 4);

    public BoStaff() {
        super("Bo Staff", 24, new int[]{5, 10, 12, 12}, true, 2);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BoStaff();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AVATAR_SPRITE;
    }

    @Override
    public int getStance() {
        return Weapon.POLEARM_STANCE;
    }
}
