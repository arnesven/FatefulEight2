package view.sprites;

import view.MyColors;


public class PortraitFrameSprite extends PortraitSprite {
    public static final int TOP = 0xFA;
    public static final int UR_CORNER = 0xFB;
    public static final int LEFT = 0xFE;
    public static final int RIGHT = 0xFD;
    public static final int UL_CORNER = 0xFC;
    public static final int LL_CORNER = 0xF9;
    public static final int LR_CORNER = 0xF8;

    public PortraitFrameSprite(int i) {
        super("portraitframe" + i, "clothes.png", i);
        setColor2(MyColors.LIGHT_GRAY);
    }

    @Override
    public void setSkinColor(MyColors color) { }
}
