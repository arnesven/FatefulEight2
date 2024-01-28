package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class TorhildAmbershard extends AdvancedAppearance {
    public TorhildAmbershard() {
        super(Race.DWARF, true, MyColors.GRAY_RED, 9, 0xC, new CharacterEyes(4, 5),
                new TorhildHairstyle(), new Beard(0xC, 0x00));
        setLipColor(MyColors.DARK_PURPLE);
    }

}
