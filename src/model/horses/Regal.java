package model.horses;

import view.MyColors;
import view.sprites.Sprite;

public class Regal extends FullBloodHorse {
    private static final Sprite SPRITE = new HorseSprite(2, 0, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.WHITE, MyColors.BEIGE);

    public Regal() {
        super("Regal", 60);
    }

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }
}