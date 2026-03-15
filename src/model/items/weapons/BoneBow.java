package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.BowAvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class BoneBow extends BowWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(6, 7, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.BEIGE);
    private static final AvatarItemSprite BOW_SPRITES =
            new BowAvatarItemSprite(0x36, MyColors.BLACK, MyColors.BROWN, MyColors.BEIGE, MyColors.BEIGE);

    public BoneBow() {
        super("Bone Bow", 25, new int[]{8,9,11,12});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BoneBow();
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return BOW_SPRITES;
    }
}
