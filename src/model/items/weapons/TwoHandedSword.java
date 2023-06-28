package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;
import view.sprites.WeaponAvatarSprite;

public class TwoHandedSword extends BladedWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(11, 0);
    private static final WeaponAvatarSprite[] TWO_HANDED_SWORD_SPRITES = makeShiftedSpriteSet(
            new WeaponAvatarSprite(0x30, MyColors.GOLD, MyColors.GRAY, MyColors.BROWN, MyColors.BEIGE));

    public TwoHandedSword() {
        super("Two-Handed Sword", 24, new int[]{6, 10, 11, 14}, true, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TwoHandedSword();
    }

    @Override
    protected WeaponAvatarSprite getOnAvatarSprite(int index) {
        return TWO_HANDED_SWORD_SPRITES[index];
    }
}
