package view.sprites;

import model.races.Race;
import view.MyColors;

public class RaisedNeckLeft extends FaceAndClothesSprite {
    public RaisedNeckLeft(MyColors detailColor, Race race) {
        super(0xA4 + (race.isSkeleton() ? 0x100 : race.isThickNeck() ? 0xE0 : 0), detailColor);
    }
}
