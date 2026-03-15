package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Wakizashi extends BladedWeapon implements PairableWeapon {

    protected static final AvatarItemSprite AVATAR_SPRITES =
            new AvatarItemSprite(0x03, MyColors.DARK_BROWN, MyColors.LIGHT_GRAY, MyColors.DARK_BROWN, MyColors.TRANSPARENT);

    private static final Sprite SPRITE = new ItemSprite(9, 11);

    public Wakizashi() {
        super("Wakizashi", 22, new int[]{8, 9, 10}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Wakizashi();
    }

    @Override
    public int getCriticalTarget() {
        return 9;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(5, 15);
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AVATAR_SPRITES;
    }
}
