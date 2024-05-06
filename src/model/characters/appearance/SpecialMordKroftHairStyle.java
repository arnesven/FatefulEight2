package model.characters.appearance;

import model.characters.preset.MordKroftHairStyle;

public class SpecialMordKroftHairStyle extends MordKroftHairStyle {
    public SpecialMordKroftHairStyle() {
        super(0xC, true, true, true, true, "Samurai");
    }

    @Override
    public int getNormalHair() {
        return 0x23;
    }

    @Override
    public int getBackHairOnly() {
        return 0x07;
    }

    @Override
    public int getFullBackHair() {
        return 0x33;
    }

    @Override
    public int getHalfBackHair() {
        return 0x34;
    }
}
