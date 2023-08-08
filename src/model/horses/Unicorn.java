package model.horses;

import view.MyColors;
import view.sprites.Sprite;

public class Unicorn extends FullBloodHorse {
    private static final Sprite SPRITE = new HorseSprite(2, 2, MyColors.PEACH, MyColors.WHITE, MyColors.PINK, MyColors.BEIGE);

    public Unicorn() {
        super("Unicorn", 0);
    }

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Horse copy() {
        return new Unicorn();
    }
}
