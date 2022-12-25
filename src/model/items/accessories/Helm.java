package model.items.accessories;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Helm extends HeadGearItem {
    private static final Sprite SPRITE = new ItemSprite(12, 9, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY);

    public Helm() {
        super("Helm", 16);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Helm();
    }

    @Override
    public int getAP() {
        return 2;
    }

    @Override
    public boolean isHeavy() {
        return true;
    }
}
