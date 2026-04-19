package model.items;

import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class SteelClaws extends BrawlingWeapon {
    private static final Sprite SPRITE = new ItemSprite(1, 8, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY);
    private static final AvatarItemSprite AVATAR_SPRITE =
            new AvatarItemSprite(0x50, MyColors.GRAY, MyColors.GRAY, MyColors.GRAY, MyColors.BEIGE);

    public SteelClaws() {
        super("Steel Claws", 15, new int[]{7, 7, 13}, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new SteelClaws();
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AVATAR_SPRITE;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(2, 8, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY);
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
