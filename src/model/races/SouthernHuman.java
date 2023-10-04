package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.PortraitSprite;

public class SouthernHuman extends Race {
    public SouthernHuman() {
        super("Human", MyColors.GRAY_RED, 0, 0,
                new Skill[]{Skill.Acrobatics, Skill.Persuade, Skill.SeekInfo},
                "Southern humans are prevalent in the south part of the world. " +
                        "Their attributes are notably average in many regards. Southern humans take up " +
                        "all kinds of professions but it is not " +
                        "uncommon for them to be Assassins, Sorcerers, Wizards, Amazons and Nobles.");
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
