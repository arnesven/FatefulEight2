package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.BowAvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class HuntersBow extends BowWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(4, 7);
    private static final AvatarItemSprite BOW_SPRITES =
            new BowAvatarItemSprite(0x36, MyColors.BLACK, MyColors.BROWN, MyColors.BROWN, MyColors.BROWN);

    public HuntersBow() {
        super("Hunter's Bow", 25, new int[]{8,9,11,14});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new HuntersBow();
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return BOW_SPRITES;
    }
}
