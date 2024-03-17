package model.horses;

import view.MyColors;
import view.sprites.HorseSprite;
import view.sprites.Sprite;

public class Pony extends Horse {
    private static final Sprite SPRITE = new HorseSprite(0, 0, MyColors.BROWN, MyColors.BEIGE, MyColors.DARK_BROWN, MyColors.YELLOW);

    public Pony() {
        super("Pony", "Pony", 35, MyColors.BROWN);
    }

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    public String getInfo() {
        return "Can only be ridden by halflings and dwarves.";
    }

    @Override
    public Horse copy() {
        return new Pony();
    }
}
