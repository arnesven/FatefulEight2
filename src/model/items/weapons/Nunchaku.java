package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Nunchaku extends BluntWeapon implements PairableWeapon {

    private static final Sprite SPRITE = new ItemSprite(5, 11);
    private static final AvatarItemSprite AVATAR_SPRITE = new AvatarItemSprite(
            0x13, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.BROWN, MyColors.PINK);


    public Nunchaku() {
        super("Nunchaku", 16, new int[]{7, 8, 9}, false, +1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Nunchaku();
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
    public Sprite makePairSprite() {
        return new ItemSprite(3, 16);
    }
}
