package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.PortraitSprite;

public class SouthernHuman extends Race {
    public SouthernHuman() {
        super("Human", MyColors.GRAY_RED, 0, 0,
                new Skill[]{Skill.Acrobatics, Skill.Persuade, Skill.SeekInfo});
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
    public String getQualifiedName() {
        return getName() + " (South)";
    }
}
