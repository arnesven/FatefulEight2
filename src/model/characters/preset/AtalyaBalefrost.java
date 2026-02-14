package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class AtalyaBalefrost extends AdvancedAppearance {
    public AtalyaBalefrost() {
        super(Race.HIGH_ELF, true, MyColors.YELLOW, 8, 0xB, new CharacterEyes(1, 0xB, "", 0, 1),
                new ShortFemaleHair("Atalya"), null);
    }
}
