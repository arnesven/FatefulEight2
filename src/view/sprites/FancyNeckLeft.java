package view.sprites;

import model.races.Race;
import view.MyColors;

public class FancyNeckLeft extends FaceAndClothesSprite {
    public FancyNeckLeft(MyColors color1, MyColors color2, Race race) {
        super(0xA2 + (race.isThickNeck() ? 0xE0 : 0), color1);
        setColor3(color2);
    }
}
