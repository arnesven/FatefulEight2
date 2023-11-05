package view.sprites;

import model.races.Race;
import view.MyColors;

public class RaisedNeckRight extends FaceAndClothesSprite {
    public RaisedNeckRight(MyColors detailColor, Race race) {
        super(0xB4 + (race.isSkeleton() ? 0x100 : race.isThickNeck() ? 0xE0 : 0), detailColor);
    }
}
