package model.items.weapons;

import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.BowAvatarItemSprite;

public abstract class SmallBow extends BowWeapon {

    private static final AvatarItemSprite BOW_SPRITES =
            new BowAvatarItemSprite(0x33, MyColors.BLACK, MyColors.BROWN, MyColors.PINK, MyColors.BEIGE);

    public SmallBow(String name, int cost, int[] damageTable) {
        super(name, cost, damageTable);
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return BOW_SPRITES;
    }
}
