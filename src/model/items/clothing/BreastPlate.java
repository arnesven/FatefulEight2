package model.items.clothing;

import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class BreastPlate extends SuperHeavyArmorClothing {
    private static final Sprite SPRITE = new ItemSprite(8, 2);

    public BreastPlate() {
        super("Breast Plate", 40, 6);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BreastPlate();
    }
}
