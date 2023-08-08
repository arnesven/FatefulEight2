package model.horses;

import view.MyColors;
import view.sprites.Sprite;

public class Prancer extends FullBloodHorse {
    private static final Sprite SPRITE = new HorseSprite(1, 0, MyColors.BLACK, MyColors.DARK_BROWN, MyColors.BROWN, MyColors.DARK_RED);

    public Prancer() {
        super("Prancer", 50);
    }

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Horse copy() {
        return new Prancer();
    }
}
