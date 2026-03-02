package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class Glaive extends PolearmWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(4, 4);

    private static final AvatarItemSprite[] POLEARM_SPRITES = makeShiftedSpriteSet(
            new FixedAvatarItemSprite( 0x91, MyColors.BROWN, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.LIGHT_GRAY));


    public Glaive() {
        super("Glaive", 26, new int[]{8,8,10,10,10});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Glaive();
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return POLEARM_SPRITES[index];
    }
}
