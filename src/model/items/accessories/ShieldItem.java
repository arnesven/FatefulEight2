package model.items.accessories;

import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.Sprite;

public abstract class ShieldItem extends OffhandItem {
    private final Sprite SHIELD_SPRITES =
            new AvatarItemSprite(0x70, MyColors.GRAY, MyColors.DARK_GRAY, MyColors.PINK, MyColors.BEIGE);

    public ShieldItem(String name, int cost, boolean isHeavy, int blockValue) {
        super(name, cost, isHeavy, blockValue);
    }

    @Override
    public Sprite getOnAvatarSprite() {
        return SHIELD_SPRITES;
    }

    @Override
    public String getSound() {
        return "wood";
    }

    @Override
    public int getWeight() {
        return 3000;
    }
}
