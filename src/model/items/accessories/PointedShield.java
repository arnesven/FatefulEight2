package model.items.accessories;

import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.Sprite;

public abstract class PointedShield extends ShieldItem {

    private static final Sprite SHIELD_SPRITES = new AvatarItemSprite(0x73, MyColors.RED, MyColors.DARK_RED, MyColors.PINK, MyColors.BEIGE);

    public PointedShield(String name, int cost, boolean isHeavy) {
        super(name, cost, isHeavy, 2);
    }

    @Override
    public Sprite getOnAvatarSprite() {
        return SHIELD_SPRITES;
    }
}
