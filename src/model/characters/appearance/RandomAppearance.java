package model.characters.appearance;

import model.races.Race;
import util.MyRandom;

public class RandomAppearance extends AdvancedAppearance {
    public RandomAppearance(Race race) {
        super(race, MyRandom.randInt(2) == 0, HairStyle.randomHairColor(), 3, 6, new NormalBigEyes(), HairStyle.randomHairStyle(), null);
    }
}
