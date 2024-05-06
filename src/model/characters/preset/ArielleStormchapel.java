package model.characters.preset;

import model.characters.FemaleLongHairStyle;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.Beard;
import model.characters.appearance.CharacterEyes;
import model.characters.appearance.SmallEyesWithBangs;
import model.races.Race;
import view.MyColors;

public class ArielleStormchapel extends AdvancedAppearance {
    public ArielleStormchapel() {
        super(Race.NORTHERN_HUMAN, true, MyColors.DARK_BROWN, 6, 5, new SmallEyesWithBangs(),
                new FemaleLongHairStyle("Arielle"), new Beard(0xB, 0x00));
        setMascaraColor(MyColors.DARK_BLUE);
    }
}
