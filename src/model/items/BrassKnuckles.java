package model.items;

import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class BrassKnuckles extends BrawlingWeapon {
    private static final AvatarItemSprite AVATAR_SPRITE =
            new AvatarItemSprite(0x50, MyColors.GOLD, MyColors.TRANSPARENT, MyColors.TRANSPARENT, MyColors.BEIGE);

    private static final Sprite SPRITE = new ItemSprite(1, 8, MyColors.GOLD, MyColors.BLACK, MyColors.BLACK);

    public BrassKnuckles() {
        super("Brass Knuckles", 6, new int[]{6, 10}, false);
    }

    @Override
    public int getSpeedModifier() {
        return -2;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BrassKnuckles();
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AVATAR_SPRITE;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(2, 8, MyColors.GOLD, MyColors.BLACK, MyColors.BLACK);
    }
}
