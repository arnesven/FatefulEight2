package model.races;

import model.characters.appearance.SlenderNeck;
import model.characters.appearance.TorsoNeck;
import model.classes.Skill;
import view.MyColors;
import view.sprites.FaceSprite;
import view.sprites.FaceSpriteWithHair;
import view.sprites.PortraitSprite;

public class ElvenRace extends Race {

    public ElvenRace(String name, MyColors color, Skill[] skillBonuses, String description) {
        super(name, color, -1, +1, 20, skillBonuses, description);
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
    public TorsoNeck makeNeck() {
        return new SlenderNeck();
    }
}
