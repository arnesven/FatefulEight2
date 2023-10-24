package view.sprites;

import model.races.Race;
import view.MyColors;

public class FancyNeckRight extends FaceAndClothesSprite {
    public FancyNeckRight(MyColors color1, MyColors color2, Race race) {
        super(0xB2 + (race.isThickNeck() ? 0xE0 : 0), color1);
        setColor3(color2);
    }
}
