package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.PortraitSprite;

public class DogRace extends Race {
    protected DogRace() {
        super("Dog", MyColors.BROWN, 0, 0, 0, new Skill[]{}, "");
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return null;
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return null;
    }

    @Override
    public int getInitialAttitudeFor(Race race) {
        return 0;
    }
}
