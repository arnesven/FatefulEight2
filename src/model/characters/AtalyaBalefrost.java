package model.characters;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class AtalyaBalefrost extends AdvancedAppearance {
    public AtalyaBalefrost() {
        super(Race.HIGH_ELF, true, MyColors.YELLOW, 8, 0xB, new CharacterEyes(1, 0xB),
                new ShortFemaleHair(), null);
    }
}
