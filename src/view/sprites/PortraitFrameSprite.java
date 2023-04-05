package view.sprites;

import view.MyColors;


public class PortraitFrameSprite extends PortraitSprite {
    public static final int TOP = 0xFA;
    public static final int UR_CORNER = 0xFB;
    public static final int LEFT = 0xFE;
    public static final int RIGHT = 0xFD;
    public static final int UL_CORNER = 0xFC;

    public PortraitFrameSprite(int i) {
        super("portraitframe" + i, "clothes.png", i);
        setColor2(MyColors.LIGHT_GRAY);
    }

    @Override
    public void setSkinColor(MyColors color) { }
}
