package model.states.fishing;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class PikeFish extends Fish {

    private static final Sprite SPRITE = new ItemSprite(11, 4, MyColors.LIGHT_GREEN, MyColors.TAN, MyColors.BEIGE);

    public PikeFish() {
        super("Pike", 1, 500, 2000, 8);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new PikeFish();
    }
}
