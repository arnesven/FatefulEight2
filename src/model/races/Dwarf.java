package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.PortraitSprite;

public class Dwarf extends Race {
    public Dwarf() {
        super("Dwarf", MyColors.PINK, +2, -2,
                new Skill[]{Skill.Axes, Skill.Endurance, Skill.Labor});
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
    public boolean isShort() {
        return true;
    }
}
