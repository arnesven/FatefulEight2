package model.items.accessories;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class AnkhPendant extends JewelryItem {
    private static final Sprite SPRITE =  new ItemSprite(4, 9, MyColors.LIGHT_GRAY, MyColors.BLUE);

    public AnkhPendant() {
        super("Ankh Pendant", 18);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getHealthBonus() {
        return 1;
    }

    @Override
    public Item copy() {
        return new AnkhPendant();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
