package view.sprites;

import model.races.Race;
import view.MyColors;

public class NeckLeft extends FaceAndClothesSprite {
    public NeckLeft(MyColors color, Race race) {
        super(0xA0 + (race.isThickNeck() ? 0xE0 : 0), color);
    }
}
