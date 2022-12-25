package model.items.accessories;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class ShinyAmulet extends JewelryItem {

    private static final Sprite SPRITE = new ItemSprite(0, 9, MyColors.YELLOW, MyColors.DARK_RED);

    public ShinyAmulet() {
        super("Shiny Amulet", 14);
    }

    @Override
    public int getAP() {
        return 1;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ShinyAmulet();
    }
}
