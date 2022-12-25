package model.items.accessories;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class GoldRing extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(5, 9, MyColors.GOLD, MyColors.LIGHT_BLUE);

    public GoldRing() {
        super("Gold Ring", 18);
    }

    @Override
    public int getHealthBonus() {
        return 1;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new GoldRing();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
