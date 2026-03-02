package model.items.weapons;

import model.items.Item;
import model.items.PirateItem;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class BoatHook extends PolearmWeapon implements PirateItem {
    private static final Sprite SPRITE = new TwoHandedItemSprite(11, 16);

    private static final AvatarItemSprite AVATAR_SPRITE =
            new FixedAvatarItemSprite( 0x93, MyColors.BROWN, MyColors.GRAY, MyColors.TRANSPARENT, MyColors.TRANSPARENT);


    public BoatHook() {
        super("Boat Hook", 18, new int[]{8, 9, 10});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getCriticalTarget() {
        return 9;
    }

    @Override
    public Item copy() {
        return new BoatHook();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AVATAR_SPRITE;
    }
}
