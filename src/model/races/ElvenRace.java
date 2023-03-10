package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.FaceSprite;
import view.sprites.FaceSpriteWithHair;
import view.sprites.PortraitSprite;

public class ElvenRace extends Race {

    public ElvenRace(String name, MyColors color, Skill[] skillBonuses) {
        super(name, color, -1, +1, skillBonuses);
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
