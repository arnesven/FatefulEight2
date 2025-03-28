package model.races;

import model.characters.appearance.SlenderNeck;
import model.characters.appearance.TorsoNeck;
import model.classes.Skill;
import view.MyColors;
import view.sprites.FaceSprite;
import view.sprites.FaceSpriteWithHair;
import view.sprites.PortraitSprite;

public abstract class ElvenRace extends Race {

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
    public TorsoNeck makeNeck(boolean gender) {
        return new SlenderNeck();
    }

    @Override
    public int getInitialAttitudeFor(Race race) {
        if (race.id() == id()) {
            return POSITIVE_ATTITUDE;
        }
        if (race instanceof ElvenRace) {
            return SLIGHT_DISLIKE_ATTITUDE;
        }
        return DISLIKE_ATTITUDE;
    }

    @Override
    public String getPlural() {
        String[] splits = getName().split(" ");
        return splits[0] + " elves";
    }

    @Override
    public abstract String getShortDescription();
}
