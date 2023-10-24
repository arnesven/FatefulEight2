package model.races;

import view.MyColors;
import view.sprites.*;

import javax.print.attribute.standard.MediaSize;

public enum Shoulders {
    NORMAL,
    NARROW,
    BROAD;

    public void makeNakedShoulders(PortraitSprite[][] grid) {
        if (this == NARROW) {
            grid[0][5] = PortraitSprite.FRAME_LEFT;
            grid[0][6] = PortraitSprite.FRAME_LL_CORNER;

            grid[1][5] = PortraitSprite.NARROW_SHOULDER_LEFT_TOP;
            grid[1][6] = PortraitSprite.NARROW_SHOULDER_LEFT_BOTTOM;

            grid[5][5] = PortraitSprite.NARROW_SHOULDER_RIGHT_TOP;
            grid[5][6] = PortraitSprite.NARROW_SHOULDER_RIGHT_BOTTOM;

            grid[6][5] = PortraitSprite.FRAME_RIGHT;
            grid[6][6] = PortraitSprite.FRAME_LR_CORNER;
        } else {
            if (this ==  Shoulders.NORMAL) {
                grid[0][5] = PortraitSprite.SHOULDER_LEFT_TOP;
                grid[6][5] = PortraitSprite.SHOULDER_RIGHT_TOP;
            } else { // Shoulders.BROAD
                grid[0][5] = PortraitSprite.BROAD_SHOULDER_LEFT_TOP;
                grid[6][5] = PortraitSprite.BROAD_SHOULDER_RIGHT_TOP;
            }

            grid[0][6] = PortraitSprite.SHOULDER_LEFT_BOTTOM;
            grid[1][5] = PortraitSprite.SHOULDER_TOP;
            grid[1][6] = PortraitSprite.FILLED_BLOCK_CLOTHES;
            grid[5][5] = PortraitSprite.SHOULDER_TOP;
            grid[5][6] = PortraitSprite.FILLED_BLOCK_CLOTHES;
            grid[6][6] = PortraitSprite.SHOULDER_RIGHT_BOTTOM;
        }
    }

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

