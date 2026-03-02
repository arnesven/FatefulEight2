package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class Trident extends PolearmWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(3, 4);

    private static final AvatarItemSprite AVATAR_SPRITE =
            new FixedAvatarItemSprite( 0x94, MyColors.BROWN, MyColors.GRAY, MyColors.PINK, MyColors.TRANSPARENT);

    public Trident() {
        super("Trident", 18, new int[]{8,8,10});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Trident();
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AVATAR_SPRITE;
    }
}
