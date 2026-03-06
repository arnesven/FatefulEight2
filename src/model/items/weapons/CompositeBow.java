package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class CompositeBow extends BowWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(2, 7);

    private static final AvatarItemSprite BOW_SPRITES =
            new AvatarItemSprite(0x36, MyColors.BLACK, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.TRANSPARENT);


    public CompositeBow() {
        super("Composite Bow", 23, new int[]{7, 7, 13});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new CompositeBow();
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return BOW_SPRITES;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
