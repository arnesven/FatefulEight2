package model.horses;

import view.MyColors;
import view.sprites.Sprite;

public class Sphinx extends FullBloodHorse {
    private static final Sprite SPRITE = new HorseSprite(0, 1, MyColors.BLACK, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.YELLOW);

    public Sphinx() {
        super("Sphinx", 48);
    }

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }
}
