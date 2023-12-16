package model.races;

import model.characters.appearance.TorsoNeck;
import model.classes.Skill;
import view.MyColors;
import view.sprites.PortraitSprite;

public abstract class HumanRace extends Race {
    public HumanRace(String name, MyColors color, Skill[] skillBonuses, String description) {
        super(name, color, 0, 0, 20, skillBonuses, description);
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return Race.normalLeftEar(hairColor);
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return Race.normalRightEar(hairColor);
    }

    @Override
    public int getInitialAttitudeFor(Race race) {
        if (race.id() == id()) {
            return POSITIVE_ATTITUDE;
        }
        return 0;
    }
}
