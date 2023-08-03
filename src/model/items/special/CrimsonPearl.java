package model.items.special;

import model.items.Item;
import view.MyColors;
import view.sprites.Sprite;

public class CrimsonPearl extends PearlItem {
    private static final Sprite SPRITE = new PearlSprite(MyColors.DARK_RED, MyColors.GRAY_RED, MyColors.RED);

    public CrimsonPearl() {
        super("Crimson Pearl", 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new CrimsonPearl();
    }

}
