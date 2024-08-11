package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.PortraitSprite;

public class FrogmanRace extends Race {
    protected FrogmanRace() {
        super("Frogmen", MyColors.GREEN, 0, 0, 10, new Skill[]{},
                "Frogmen are a semi-intelligent tribal race prevalent in swamps, bogs and " +
                        "along rivers. They have large heads with fish-like eyes, bulbous bodies to which " +
                        "skinny extremities are attached. They form primitive societies and often avoid contact with " +
                        "members of other races.");
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
