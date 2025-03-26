package model.items.weapons;

import model.items.Item;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class Musket extends SlugThrowers {

    private static final Sprite SPRITE = new TwoHandedItemSprite(14, 16);

    public Musket() {
        super("Musket", 65, new int[]{7, 7, 7, 7, 10}, true);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 3400;
    }

    @Override
    public Item copy() {
        return new Musket();
    }
}
