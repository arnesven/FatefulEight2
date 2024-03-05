package model.characters.preset;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.Beard;
import model.characters.appearance.CharacterEyes;
import model.characters.appearance.ShortFemaleHair;
import model.races.Race;
import view.MyColors;

public class AudreyPuddle extends AdvancedAppearance {
    public AudreyPuddle() {
        super(Race.HALFLING, true, MyColors.DARK_RED, 3, 0xA,
                new CharacterEyes(0xE, 0xF), new ShortFemaleHair("Audrey"), new Beard(0xA, 0x00));
        setLipColor(MyColors.RED);
    }
}
