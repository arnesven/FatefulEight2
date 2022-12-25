package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.FaceSpriteWithHair;
import view.sprites.PortraitSprite;

public class Halfling extends Race {
    public Halfling() {
        super("Halfling", MyColors.PEACH, -2, 2, new Skill[]{
                Skill.Entertain, Skill.Security, Skill.Sneak
        });
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x74, hairColor);
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x84, hairColor);
    }

    @Override
    public boolean isShort() {
        return true;
    }
}
