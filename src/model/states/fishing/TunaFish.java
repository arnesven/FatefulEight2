package model.states.fishing;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class TunaFish extends Fish {

    private static final Sprite SPRITE = new ItemSprite(10, 4, MyColors.GRAY, MyColors.PINK, MyColors.LIGHT_BLUE);

    public TunaFish() {
        super("Tuna", 5, 2000, 6000, 10);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TunaFish();
    }
}
