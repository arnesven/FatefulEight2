package model.items;

import model.items.clothing.Clothing;
import view.sprites.Sprite;
import view.sprites.HigherTierItemSprite;

public class HigherTierClothing extends Clothing {
    private static final int COST_MULTIPLIER = 3;
    private final Clothing innerItem;
    private final HigherTierItemSprite sprite;
    private final int tier;

    public HigherTierClothing(Clothing innerItem, int tier) {
        super(Item.getHigherTierPrefix(tier) + " " + innerItem.getName(),
                innerItem.getCost()*(tier*2+1), innerItem.getAP() + tier, innerItem.isHeavy());
        this.innerItem = innerItem;
        this.sprite = new HigherTierItemSprite(innerItem.getSpriteForHigherTier(tier), tier);
        this.tier = tier;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new HigherTierClothing((Clothing) innerItem.copy(), tier);
    }
}
