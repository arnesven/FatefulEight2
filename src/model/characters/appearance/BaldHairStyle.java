package model.characters.appearance;

public class BaldHairStyle extends HairStyle {

    public BaldHairStyle() {
        super(false, false, false, false, "Bald");
    }

    @Override
    public int getForeheadRight() {
        return 0x10;
    }
    @Override
    public int getForeheadCenter() {
        return 0xFF;
    }
    @Override
    public int getForeheadLeft() {
        return 0x00;
    }
    @Override
    public int getHeadTopRight() {
        return 0x11;
    }

    @Override
    public int getNormalHair() {
        return 0x00;
    }

    @Override
    public int getBackHairOnly() {
        return 0x00;
    }

    @Override
    public int getHeadTop() {
        return 0xFE;
    }
    @Override
    public int getHeadTopLeft() { return 0x01; }

}
