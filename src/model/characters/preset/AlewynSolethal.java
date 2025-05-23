package model.characters.preset;

import model.characters.FemaleLongHairStyle;
import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class AlewynSolethal extends AdvancedAppearance {
    public AlewynSolethal() {
        super(Race.HIGH_ELF, true, MyColors.GRAY, CharacterCreationView.mouthSet[6],
                CharacterCreationView.noseSet[4], CharacterEyes.allEyes[2],
                new BunsWithLongHairStyle(new FemaleLongHairStyle("Buns/Long"), true, "Buns/Long"),
                Beard.allBeards[11]);
        addFaceDetail(new GlassesDetail());
        setDetailColor(MyColors.GOLD);
        addFaceDetail(new RoundEarringsDetail());
        setDetailColor(MyColors.GOLD);
        setLipColor(MyColors.LIGHT_RED);
    }
}
