package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class Halberd extends PolearmWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(5, 4);

    private static final AvatarItemSprite AVATAR_SPRITE =
            new FixedAvatarItemSprite( 0x92, MyColors.BROWN, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.TRANSPARENT);


    public Halberd() {
        super("Halberd", 23, new int[]{6,8,10,10});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Halberd();
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AVATAR_SPRITE;
    }
}
