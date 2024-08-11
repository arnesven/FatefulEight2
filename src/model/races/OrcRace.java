package model.races;

import model.characters.appearance.*;
import model.classes.Skill;
import view.MyColors;
import view.sprites.FaceSpriteWithHair;
import view.sprites.PortraitSprite;

public class OrcRace extends Race {

    protected OrcRace() {
        super("Orc", MyColors.ORC_GREEN, 2, -2, 30, new Skill[]{}, "Unused");
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x71, hairColor);
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x81, hairColor);
    }

    @Override
    public Shoulders makeShoulders(boolean gender) {
        return new HunkyShoulders(gender);
    }

    @Override
    public TorsoNeck makeNeck(boolean gender) {
        return new HunkyNeck();
    }

    @Override
    public MyColors getMouthDefaultColor() {
        return MyColors.DARK_GRAY;
    }

    @Override
    public int getInitialAttitudeFor(Race race) {
        return 0;
    }

    @Override
    public AdvancedAppearance makeAppearance(Race race, boolean gender, MyColors hairColor,
                                             int mouth, int nose, CharacterEyes characterEyes,
                                             HairStyle hairStyle, Beard beard) {
        return new OrcAppearance(gender, hairColor, mouth, nose, characterEyes, hairStyle);
    }
}
