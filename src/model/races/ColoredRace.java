package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.PortraitSprite;

public class ColoredRace extends Race {
    private final Race innerRace;

    public ColoredRace(MyColors skinColor, Race innerRace) {
        super("Colored Race", skinColor, 0, 0, 0,
                new Skill[0], "unused");
        this.innerRace = innerRace;
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return innerRace.getLeftEar(hairColor);
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return innerRace.getRightEar(hairColor);
    }

    @Override
    public int getInitialAttitudeFor(Race race) {
        return race.getInitialAttitudeFor(race);
    }

    @Override
    public boolean isShort() {
        return innerRace.isShort();
    }
}
