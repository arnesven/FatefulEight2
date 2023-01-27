package model.characters;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.Beard;
import model.characters.appearance.CharacterEyes;
import model.races.Race;
import view.MyColors;

public class ArielleStormchapel extends AdvancedAppearance {
    public ArielleStormchapel() {
        super(Race.NORTHERN_HUMAN, true, MyColors.DARK_BROWN, 6, 5, new CharacterEyes(2,3),
                new FemaleLongHairStyle(), new Beard(0xB, 0x00));
    }
}
