package model.horses;

import view.MyColors;
import view.sprites.HorseSprite;
import view.sprites.Sprite;

public class Regal extends Steed {
    private static final Sprite SPRITE = new HorseSprite(2, 0, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.WHITE, MyColors.BEIGE);

    public Regal() {
        super("Regal", 60, MyColors.BEIGE);
    }

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Horse copy() {
        return new Regal();
    }
}
