package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.PortraitSprite;

import java.awt.*;

public class AllRaces extends Race {
    protected AllRaces() {
        super("All", MyColors.WHITE, 0, 0, 20, new Skill[]{}, "");
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
        return 0; // unused
    }
}
