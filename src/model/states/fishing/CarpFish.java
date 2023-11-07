package model.states.fishing;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class CarpFish extends Fish {

    private static final Sprite SPRITE = new ItemSprite(11, 4, MyColors.LIGHT_GRAY, MyColors.GRAY, MyColors.GRAY);

    public CarpFish() {
        super("Carp", 2, 500, 1500, 8);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new CarpFish();
    }
}
