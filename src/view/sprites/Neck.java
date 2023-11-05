package view.sprites;

import model.races.Race;
import view.MyColors;

public class Neck extends FaceAndClothesSprite {
    public Neck(MyColors color, Race race) {
        super(race.isSkeleton() ? 0x18C : 0x90, color);
    }
}
