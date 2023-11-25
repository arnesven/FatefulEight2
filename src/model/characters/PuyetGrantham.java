package model.characters;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterEyes;
import model.characters.appearance.EyePatchDetail;
import model.characters.appearance.HairStyle3x2;
import model.races.Race;
import view.MyColors;

public class PuyetGrantham extends AdvancedAppearance {
    public PuyetGrantham() {
        super(Race.HALF_ORC, true, MyColors.DARK_GRAY, 0, 2, new CharacterEyes(2, 3),
                new HairStyle3x2(0x9, true, true, true, true, 0x06, 0x07), null);
        setFaceDetail(new EyePatchDetail());
        setDetailColor(MyColors.BLACK);
        setLipColor(MyColors.DARK_RED);
        setMascaraColor(MyColors.LIGHT_BLUE);
    }
}
