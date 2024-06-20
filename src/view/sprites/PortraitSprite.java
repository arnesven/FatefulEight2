package view.sprites;

import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public abstract class PortraitSprite extends Sprite8x8 {

    public static final PortraitSprite FRAME_UL_CORNER = new PortraitFrameSprite(PortraitFrameSprite.UL_CORNER);
    public static final PortraitSprite FRAME_TOP = new PortraitFrameSprite(PortraitFrameSprite.TOP);
    public static final PortraitSprite FRAME_UR_CORNER = new PortraitFrameSprite(PortraitFrameSprite.UR_CORNER);
    public static final PortraitSprite FRAME_LL_CORNER = new PortraitFrameSprite(PortraitFrameSprite.LL_CORNER);
    public static final PortraitSprite FRAME_LR_CORNER = new PortraitFrameSprite(PortraitFrameSprite.LR_CORNER);
    public static final PortraitSprite FRAME_LEFT = new PortraitFrameSprite(PortraitFrameSprite.LEFT);
    public static final PortraitSprite FRAME_RIGHT = new PortraitFrameSprite(PortraitFrameSprite.RIGHT);
    public static final PortraitSprite BLACK_BLOCK = FilledBlockSprite.BLACK;

    public PortraitSprite(String name, String mapPath, int number, List<Sprite> layers) {
        super(name, mapPath, number, layers);
    }

    public PortraitSprite(String name, String mapPath, int number) {
        this(name, mapPath, number, new ArrayList<>());
    }

    public abstract void setSkinColor(MyColors color);
}
