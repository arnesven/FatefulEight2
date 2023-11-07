package model.states.fishing;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class BreamFish extends Fish {
    private static final Sprite SPRITE = new ItemSprite(12, 4, MyColors.LIGHT_GRAY, MyColors.TAN, MyColors.BLACK);

    public BreamFish() {
        super("Bream", 1, 100, 500, 7);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BreamFish();
    }
}
