package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class Spear extends PolearmWeapon {

    private static final AvatarItemSprite POLEARM_SPRITES =
            new FixedAvatarItemSprite( 0x90, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.TRANSPARENT, MyColors.GRAY);

    private static final Sprite SPRITE = new TwoHandedItemSprite(1, 4);

    public Spear() {
        super("Spear", 20, new int[]{9, 9, 9, 9});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Spear();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return POLEARM_SPRITES;
    }
}
