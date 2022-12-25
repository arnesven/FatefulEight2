package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.PortraitSprite;

public class NorthernHuman extends HumanRace {
    protected NorthernHuman() {
        super("Human", MyColors.PINK, 0, 0,
                new Skill[]{Skill.Persuade, Skill.Search, Skill.SeekInfo});
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
        return getName() + " (North)";
    }
}
