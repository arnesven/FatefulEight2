package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.*;

public class GrandMaul extends BluntWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(9, 1);

    private static final AvatarItemSprite ON_AVATAR_SPRITE =
            new FixedAvatarItemSprite(0x2F, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.TRANSPARENT, MyColors.TRANSPARENT);


    public GrandMaul() {
        super("Grand Maul", 20, new int[]{7,9,12,13,15}, true, -2);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new GrandMaul();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return ON_AVATAR_SPRITE;
    }

    @Override
    public int getStance() {
        return Weapon.TWO_HANDED_STANCE;
    }
}
