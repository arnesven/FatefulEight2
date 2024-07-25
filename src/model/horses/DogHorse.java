package model.horses;

import view.MyColors;
import view.sprites.HorseSprite;
import view.sprites.Sprite;

public class DogHorse extends Horse {

    private static final Sprite SPRITE = new HorseSprite(2, 3, MyColors.BLACK, MyColors.BEIGE, MyColors.BROWN, MyColors.DARK_PURPLE);

    public DogHorse() {
        super("Dog", "Dog", 0, MyColors.BROWN);
    }

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getInfo() {
        return "Not mountable by any character and does not add to your carrying capacity. However, dogs " +
                "may be useful for finding things out in the field, and for warning you when enemies are near.";
    }

    @Override
    public Horse copy() {
        return new DogHorse();
    }
}
