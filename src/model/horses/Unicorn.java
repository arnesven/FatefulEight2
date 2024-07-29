package model.horses;

import view.MyColors;
import view.sprites.HorseSprite;
import view.sprites.Sprite;

public class Unicorn extends Steed {
    private static final HorseSprite SPRITE = new HorseSprite(2, 2, MyColors.PEACH, MyColors.WHITE, MyColors.PINK, MyColors.BEIGE);

    public Unicorn() {
        super("Unicorn", 0, MyColors.BEIGE);
    }

    @Override
    public HorseSprite getSprite() {
        return SPRITE;
    }

    @Override
    public Horse copy() {
        return new Unicorn();
    }
}
