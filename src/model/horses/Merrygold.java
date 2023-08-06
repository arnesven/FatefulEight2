package model.horses;

import view.MyColors;
import view.sprites.Sprite;

public class Merrygold extends FullBloodHorse {

    private static final Sprite SPRITE = new HorseSprite(1, 1, MyColors.BLACK, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.BEIGE);

    public Merrygold() {
        super("Merrygold", 50);
    }

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }
}
