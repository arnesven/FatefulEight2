package model.horses;

import view.MyColors;
import view.sprites.HorseSprite;
import view.sprites.Sprite;

public class Faxe extends Steed {
    private static final HorseSprite SPRITE = new HorseSprite(0, 2, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.BROWN);

    public Faxe() {
        super("Faxe", 52, MyColors.GRAY);
    }

    @Override
    public HorseSprite getSprite() {
        return SPRITE;
    }

    @Override
    public Horse copy() {
        return new Faxe();
    }
}
