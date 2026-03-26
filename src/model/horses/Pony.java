package model.horses;

import view.MyColors;
import view.sprites.HorseSprite;
import view.sprites.Sprite;

public class Pony extends Horse {
    private static final HorseSprite SPRITE = new HorseSprite(0, 0, MyColors.BEIGE, MyColors.DARK_BROWN, MyColors.BROWN, MyColors.YELLOW);

    public Pony() {
        super("Pony", "Pony", 35, MyColors.BROWN);
    }

    @Override
    public HorseSprite getSprite() {
        return SPRITE;
    }

    public String getInfo() {
        return "Can only be ridden by halflings and dwarves.";
    }

    @Override
    public Horse copy() {
        return new Pony();
    }

    @Override
    public MyColors getPatchColor() {
        return MyColors.BEIGE;
    }

    @Override
    public MyColors getFaceColor() {
        return MyColors.YELLOW;
    }
}
