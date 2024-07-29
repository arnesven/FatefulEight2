package model.horses;

import view.MyColors;
import view.sprites.HorseSprite;
import view.sprites.Sprite;

public class Sphinx extends Steed {
    private static final HorseSprite SPRITE = new HorseSprite(0, 1, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.DARK_BROWN, MyColors.YELLOW);

    public Sphinx() {
        super("Sphinx", 48, MyColors.DARK_GRAY);
    }

    @Override
    public HorseSprite getSprite() {
        return SPRITE;
    }

    @Override
    public Horse copy() {
        return new Sphinx();
    }
}
