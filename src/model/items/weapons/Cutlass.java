package model.items.weapons;

import model.items.Item;
import model.items.PirateItem;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Cutlass extends LongBladedWeapon implements PairableWeapon, PirateItem {
    protected static final AvatarItemSprite AVATAR_SPRITES =
            new AvatarItemSprite(0x09, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.TRANSPARENT, MyColors.GOLD);

    private static final Sprite SPRITE = new ItemSprite(9, 16);

    public Cutlass() {
        super("Cutlass", 20, new int[]{6, 8, 12}, false, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Cutlass();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(10, 16);
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AVATAR_SPRITES;
    }
}
