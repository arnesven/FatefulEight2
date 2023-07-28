package model.items.clothing;

import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;


public class ScaleArmor extends SuperHeavyArmorClothing {
    private static final Sprite SPRITE = new ItemSprite(7, 2);

    public ScaleArmor() {
        super("Scale Armor", 42, 5);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ScaleArmor();
    }


}
