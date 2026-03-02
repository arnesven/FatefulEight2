package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class BecDeCorbin extends PolearmWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(8, 4);

    private static final AvatarItemSprite AVATAR_SPRITE =
            new FixedAvatarItemSprite( 0x93, MyColors.BROWN, MyColors.GRAY, MyColors.GRAY, MyColors.TRANSPARENT);

    public BecDeCorbin() {
        super("Bec de corbin", 24, new int[]{6, 8, 10, 10});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BecDeCorbin();
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AVATAR_SPRITE;
    }
}
