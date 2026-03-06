package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class HeavyCrossbow extends CrossbowWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(8, 6);

    private static final AvatarItemSprite ON_SPRITE = new FixedAvatarItemSprite(0x3C, MyColors.GRAY, MyColors.BROWN, MyColors.GRAY, MyColors.DARK_BROWN);

    public HeavyCrossbow() {
        super("Heavy Crossbow", 34, new int[]{8,9,10,14,14});
    }

    @Override
    public int getSpeedModifier() {
        return -1;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Item copy() {
        return new HeavyCrossbow();
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return ON_SPRITE;
    }
}
