package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.FaceSpriteWithHair;
import view.sprites.PortraitSprite;

public class WitchKingRace extends Race {
    protected WitchKingRace() {
        super("Witch King", MyColors.CYAN, 0, 0, new Skill[]{});
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x72, hairColor);
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x82, hairColor);
    }
}
