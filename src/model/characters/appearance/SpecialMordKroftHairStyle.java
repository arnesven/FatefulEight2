package model.characters.appearance;

import model.characters.MordKroftHairStyle;

public class SpecialMordKroftHairStyle extends MordKroftHairStyle {
    public SpecialMordKroftHairStyle() {
        super(0xC, true, true, true, true);
    }

    @Override
    public int getNormalHair() {
        return 0x23;
    }

    @Override
    public int getBackHairOnly() {
        return 0x07;
    }
}
