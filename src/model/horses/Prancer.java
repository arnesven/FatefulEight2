package model.horses;

import view.MyColors;
import view.sprites.HorseSprite;
import view.sprites.Sprite;

public class Prancer extends Steed {
    private static final HorseSprite SPRITE = new HorseSprite(1, 0, MyColors.BLACK, MyColors.DARK_BROWN, MyColors.BROWN, MyColors.DARK_RED);

    public Prancer() {
        super("Prancer", 50, MyColors.BROWN);
    }

    @Override
    public HorseSprite getSprite() {
        return SPRITE;
    }

    @Override
    public Horse copy() {
        return new Prancer();
    }
}
