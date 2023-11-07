package model.states.fishing;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class BassFish extends Fish {

    private static final Sprite SPRITE = new ItemSprite(12, 4, MyColors.LIGHT_GREEN, MyColors.RED, MyColors.BLACK);

    public BassFish() {
        super("Bass", 2, 700, 2800, 9);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BassFish();
    }
}
