package model.items.weapons;

import model.items.Item;
import model.items.StartingItem;
import view.sprites.Sprite;

public class LightCrossbow extends CrossbowWeapon implements StartingItem {

    private static CommonCrossbow inner = new CommonCrossbow();
    private static ShortBow bow = new ShortBow();

    public LightCrossbow() {
        super("Light Crossbow", 20, bow.getDamageTable());
    }

    @Override
    public int getSpeedModifier() {
        return -1;
    }

    @Override
    protected Sprite getSprite() {
        return inner.getSprite();
    }

    @Override
    public Item copy() {
        return new LightCrossbow();
    }
}
