package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Warhammer extends BluntWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(7, 1);

    protected static final AvatarItemSprite[] ON_AVATAR_SPRITES = makeShiftedSpriteSet(
            new AvatarItemSprite(0x84, MyColors.BROWN, MyColors.GRAY, MyColors.PEACH, MyColors.GRAY));

    public Warhammer() {
        super("Warhammer", 18, new int[]{4,7,10}, false, -1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Warhammer();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(13, 14);
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return ON_AVATAR_SPRITES[index];
    }
}
