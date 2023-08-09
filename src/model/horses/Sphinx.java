package model.horses;

import view.MyColors;
import view.sprites.Sprite;

public class Sphinx extends Steed {
    private static final Sprite SPRITE = new HorseSprite(0, 1, MyColors.BLACK, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.YELLOW);

    public Sphinx() {
        super("Sphinx", 48, MyColors.BROWN);
    }

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Horse copy() {
        return new Sphinx();
    }
}
