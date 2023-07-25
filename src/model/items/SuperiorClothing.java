package model.items;

import model.items.clothing.Clothing;
import view.sprites.Sprite;
import view.sprites.SuperiorItemSprite;

public class SuperiorClothing extends Clothing {
    private static final int COST_MULTIPLIER = 3;
    private final Clothing innerItem;
    private final SuperiorItemSprite sprite;

    public SuperiorClothing(Clothing innerItem) {
        super("Superior " + innerItem.getName(), innerItem.getCost()*COST_MULTIPLIER, innerItem.getAP() + 1, innerItem.isHeavy());
        this.innerItem = innerItem;
        this.sprite = new SuperiorItemSprite(innerItem.getSprite());
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new SuperiorClothing((Clothing) innerItem.copy());
    }
}
