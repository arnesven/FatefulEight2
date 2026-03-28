package model.items.accessories;

import model.items.BlockingItem;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.Sprite;

public abstract class OffhandItem extends Accessory implements BlockingItem {

    private final boolean heavyArmor;
    private final int block;

    public OffhandItem(String name, int cost, boolean isHeavy, int blockValue) {
        super(name, cost);
        this.heavyArmor = isHeavy;
        this.block = blockValue;
    }

    @Override
    public boolean isHeavy() {
        return heavyArmor;
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

    public abstract Sprite getOnAvatarSprite();
}
