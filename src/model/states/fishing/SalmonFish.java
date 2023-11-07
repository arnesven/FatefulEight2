package model.states.fishing;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class SalmonFish extends Fish {

    private static final Sprite SPRITE = new ItemSprite(10, 4,
            MyColors.GRAY, MyColors.PINK, MyColors.LIGHT_GRAY);

    public SalmonFish() {
        super("Salmon", 3, 1500, 3500, 10);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new SalmonFish();
    }
}
