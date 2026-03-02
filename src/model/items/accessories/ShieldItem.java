package model.items.accessories;

import model.items.BlockingItem;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.Sprite;

public abstract class ShieldItem extends Accessory implements BlockingItem {

    private final boolean heavyArmor;
    private final int block;
    private final Sprite SHIELD_SPRITES =
            new AvatarItemSprite(0x70, MyColors.GRAY, MyColors.DARK_GRAY, MyColors.PINK, MyColors.BEIGE);

    public ShieldItem(String name, int cost, boolean isHeavy, int blockValue) {
        super(name, cost);
        this.heavyArmor = isHeavy;
        this.block = blockValue;
    }

    @Override
    public boolean isHeavy() {
        return heavyArmor;
    }

    @Override
    public String getSound() {
        return "wood";
    }

    public Sprite getOnAvatarSprite() {
        return SHIELD_SPRITES;
    }

    public int getBlockChance() {
        return block;
    }

    @Override
    public String getExtraText() {
        return "Block " + getBlockChance();
    }

    @Override
    public boolean isOffHandItem() {
        return true;
    }

    @Override
    public int getWeight() {
        return 3000;
    }
}
