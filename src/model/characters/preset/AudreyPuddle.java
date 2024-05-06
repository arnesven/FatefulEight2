package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class AudreyPuddle extends AdvancedAppearance {
    public AudreyPuddle() {
        super(Race.HALFLING, true, MyColors.DARK_RED, 3, 0xA,
                new ElfinEyes(), new ShortFemaleHair("Audrey"), new Beard(0xA, 0x00));
        setLipColor(MyColors.RED);
    }
}
