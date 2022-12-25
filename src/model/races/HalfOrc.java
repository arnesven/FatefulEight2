package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.FaceSprite;
import view.sprites.FaceSpriteWithHair;
import view.sprites.PortraitSprite;

public class HalfOrc extends Race {
    protected HalfOrc() {
        super("Half-Orc", MyColors.ORC_GREEN, 1, -1,
                new Skill[]{Skill.BluntWeapons, Skill.Endurance, Skill.Survival});
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x71, hairColor);
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x81, hairColor);
    }

}
