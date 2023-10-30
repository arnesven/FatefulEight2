package model.races;

import view.MyColors;
import view.sprites.*;

import javax.print.attribute.standard.MediaSize;

public enum Shoulders {
    NORMAL,
    NARROW,
    BROAD;

    public PortraitSprite makeLeftTopSprite(MyColors color) {
        if (this == NARROW) {
            return PortraitSprite.FRAME_LEFT;
        }
        if (this == BROAD) {
            return new ClothesSprite(0x5D, color);
        }
        return new ShoulderLeftTop(color);
    }

    public PortraitSprite makeInnerLeftTopSprite(MyColors color) {
        if (this == NARROW) {
            return new ClothesSprite(0x3D, color);
        }
        return new ShoulderTop(color);
    }

    public PortraitSprite makeInnerRightTopSprite(MyColors color) {
        if (this == NARROW) {
            return new ClothesSprite(0x3E, color);
        }
        return new ShoulderTop(color);
    }

    public PortraitSprite makeRightTopSprite(MyColors color) {
        if (this == NARROW) {
            return PortraitSprite.FRAME_RIGHT;
        }
        if (this == BROAD) {
            return new ClothesSprite(0x5E, color);
        }
        return new ShoulderRightTop(color);
    }

    public PortraitSprite makeLeftBottomSprite(MyColors color) {
        if (this == NARROW) {
            return PortraitSprite.FRAME_LL_CORNER;
        }
        return new ShoulderLeftBottom(color);
    }

    public PortraitSprite makeRightBottomSprite(MyColors color) {
        if (this == NARROW) {
            return PortraitSprite.FRAME_LR_CORNER;
        }
        return new ShoulderRightBottom(color);
    }

    public PortraitSprite makeInnerLeftBottomSprite(MyColors color) {
        if (this == NARROW) {
            return new ClothesSprite(0x4D, color);
        }
        return new FilledBlockClothes(color);
    }

    public PortraitSprite makeInnerRightBottomSprite(MyColors color) {
        if (this == NARROW) {
            return new ClothesSprite(0x4E, color);
        }
        return new FilledBlockClothes(color);
    }
}

