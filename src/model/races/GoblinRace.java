package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.PortraitSprite;

public class GoblinRace extends Race {
    protected GoblinRace() {
        super("Goblin", MyColors.ORC_GREEN, 0, 0, new Skill[0],
                "Goblins are a smaller form of Orc which are common to hills, mountains and caves.");
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return null;
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return null;
    }
}
