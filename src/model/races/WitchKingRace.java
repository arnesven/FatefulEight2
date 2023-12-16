package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.FaceSpriteWithHair;
import view.sprites.PortraitSprite;

public class WitchKingRace extends Race {
    protected WitchKingRace() {
        super("Witch King", MyColors.CYAN, 0, 0, 20, new Skill[]{},
                "The Witch King is a member of an ancient race of rulers, long forgotten in this world.");
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x72, hairColor);
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x82, hairColor);
    }

    @Override
    public int getInitialAttitudeFor(Race race) {
        return 0; // unused
    }
}
