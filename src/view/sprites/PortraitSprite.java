package view.sprites;

import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public abstract class PortraitSprite extends Sprite8x8 {

    public static final PortraitSprite NECK_1 = new NakedFaceAndClothesSprite(0x90);
    public static final PortraitSprite NECK_LEFT = new NakedFaceAndClothesSprite(0xA0);
    public static final PortraitSprite NECK_RIGHT = new NakedFaceAndClothesSprite(0xB0);

    public static final PortraitSprite NECK_LEFT_THICK = new NakedFaceAndClothesSprite(0x180);
    public static final PortraitSprite NECK_RIGHT_THICK = new NakedFaceAndClothesSprite(0x190);

    public static final PortraitSprite CHEST_1 = new NakedFaceAndClothesSprite(0xC2);
    public static final PortraitSprite CHEST_2 = new NakedFaceAndClothesSprite(0xC1);

    public static final PortraitSprite SHOULDER_LEFT_TOP = new NakedClothesSprite(0x00);
    public static final PortraitSprite SHOULDER_LEFT_BOTTOM = new NakedClothesSprite(0x10);
    public static final PortraitSprite SHOULDER_RIGHT_TOP = new NakedClothesSprite(0x01);
    public static final PortraitSprite SHOULDER_RIGHT_BOTTOM = new NakedClothesSprite(0x11);

    public static final PortraitSprite NARROW_SHOULDER_LEFT_TOP = new NakedClothesSprite(0x3D);
    public static final PortraitSprite NARROW_SHOULDER_LEFT_BOTTOM = new NakedClothesSprite(0x4D);
    public static final PortraitSprite NARROW_SHOULDER_RIGHT_TOP = new NakedClothesSprite(0x3E);
    public static final PortraitSprite NARROW_SHOULDER_RIGHT_BOTTOM = new NakedClothesSprite(0x4E);

    public static final PortraitSprite BROAD_SHOULDER_LEFT_TOP = new NakedClothesSprite(0x5D);
    public static final PortraitSprite BROAD_SHOULDER_RIGHT_TOP = new NakedClothesSprite(0x5E);

    public static final PortraitSprite FILLED_BLOCK_CLOTHES = new NakedClothesSprite(0xFF);
    public static final PortraitSprite SHOULDER_TOP = new NakedClothesSprite(0x20);

    public static final PortraitSprite FRAME_UL_CORNER = new PortraitFrameSprite(PortraitFrameSprite.UL_CORNER);
    public static final PortraitSprite FRAME_TOP = new PortraitFrameSprite(PortraitFrameSprite.TOP);
    public static final PortraitSprite FRAME_UR_CORNER = new PortraitFrameSprite(PortraitFrameSprite.UR_CORNER);
    public static final PortraitSprite FRAME_LL_CORNER = new PortraitFrameSprite(PortraitFrameSprite.LL_CORNER);
    public static final PortraitSprite FRAME_LR_CORNER = new PortraitFrameSprite(PortraitFrameSprite.LR_CORNER);
    public static final PortraitSprite FRAME_LEFT = new PortraitFrameSprite(PortraitFrameSprite.LEFT);
    public static final PortraitSprite FRAME_RIGHT = new PortraitFrameSprite(PortraitFrameSprite.RIGHT);
    public static final PortraitSprite BLACK_BLOCK = new FilledBlockSprite(MyColors.BLACK);

    public PortraitSprite(String name, String mapPath, int number, List<Sprite> layers) {
        super(name, mapPath, number, layers);
    }

    public PortraitSprite(String name, String mapPath, int number) {
        this(name, mapPath, number, new ArrayList<>());
    }

    public abstract void setSkinColor(MyColors color);
}
