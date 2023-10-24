package view.sprites;

import model.races.Race;
import view.MyColors;

public class NeckRight extends FaceAndClothesSprite {
    public NeckRight(MyColors color, Race race) {
        super(0xB0 + (race.isThickNeck() ? 0xE0 : 0), color);
    }
}
