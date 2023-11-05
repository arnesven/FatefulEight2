package view.sprites;

import model.races.Race;
import view.MyColors;

public class WideNeckRight extends FaceAndClothesSprite {
    public WideNeckRight(MyColors color, Race race) {
        super(0xB3 + (race.isSkeleton() ? 0x100 : race.isThickNeck() ? 0xE0 : 0), color);
    }
}
