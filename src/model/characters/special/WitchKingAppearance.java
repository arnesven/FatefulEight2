package model.characters.special;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.ScreenHandler;
import view.party.CharacterCreationView;

public class WitchKingAppearance extends AdvancedAppearance {

    public WitchKingAppearance() {
        super(Race.WITCH_KING, false, MyColors.WHITE, CharacterCreationView.mouthSet[7],
                CharacterCreationView.noseSet[10], new WitchKingEyes(), HairStyle.allHairStyles[8], Beard.allBeards[7]);
    }

    @Override
    protected MyColors getEyeballColor() {
        return MyColors.YELLOW;
    }

    @Override
    public void drawDrawLook(ScreenHandler screenHandler, boolean left, int x, int y) { }

    @Override
    public void drawSurprised(ScreenHandler screenHandler, int x, int y) {}
}
