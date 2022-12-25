package model.items.accessories;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class SwiftRing extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(6, 9,MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.WHITE);

    public SwiftRing() {
        super("Swift Ring", 10);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getSpeedModifier() {
        return 1;
    }

    @Override
    public Item copy() {
        return new SwiftRing();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
