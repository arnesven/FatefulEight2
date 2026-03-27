package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.PortraitSprite;

public abstract class GigantoidRace extends Race {
    protected GigantoidRace(String name, MyColors color, int hpModifier, int speed, int carryCap,
                            Skill[] skillBonuses, String description) {
        super(name, color, hpModifier, speed, carryCap, skillBonuses, description);
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
