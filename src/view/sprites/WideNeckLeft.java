package view.sprites;

import model.races.Race;
import view.MyColors;

public class WideNeckLeft extends FaceAndClothesSprite {
    public WideNeckLeft(MyColors color, Race race) {
        super(0xA3 + (race.isSkeleton() ? 0x100 : race.isThickNeck() ? 0xE0 : 0), color);
    }
}
