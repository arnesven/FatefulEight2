package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class MorningStar extends BluntWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(5,1);

    protected static final AvatarItemSprite ON_AVATAR_SPRITES =
            new AvatarItemSprite(0x29, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.GRAY, MyColors.TRANSPARENT);


    public MorningStar() {
        super("Morning Star", 18, new int[]{5, 7, 10}, false, -1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new MorningStar();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(12, 14);
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return ON_AVATAR_SPRITES;
    }
}
