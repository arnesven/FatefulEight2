package model.races;

import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.SkeletonAppearance;
import view.MyColors;
import view.sprites.*;

public class NarrowShoulders extends Shoulders {
    private final PortraitSprite NARROW_SHOULDER_LEFT_TOP = new NakedClothesSprite(0x3D);
    private final PortraitSprite NARROW_SHOULDER_LEFT_BOTTOM = new NakedClothesSprite(0x4D);
    private final PortraitSprite NARROW_SHOULDER_RIGHT_TOP = new NakedClothesSprite(0x3E);
    private final PortraitSprite NARROW_SHOULDER_RIGHT_BOTTOM = new NakedClothesSprite(0x4E);
    private final PortraitSprite NARROW_SHOULDER_LEFT_BOTTOM_BUSTY = new NakedClothesSprite(0xD7);
    private final PortraitSprite NARROW_SHOULDER_RIGHT_BOTTOM_BUSTY = new NakedClothesSprite(0xD8);

    private static final PortraitSprite SHOULDER_LEFT_TOP = new FaceAndClothesSprite(0x18A, MyColors.CYAN);
    private static final PortraitSprite SHOULDER_RIGHT_TOP = new FaceAndClothesSprite(0x18E, MyColors.CYAN);
    private static final PortraitSprite NARROW_SHOULDER_LEFT = new FaceAndClothesSprite(0x1BB, MyColors.CYAN);
    private static final PortraitSprite NARROW_SHOULDER_RIGHT = new FaceAndClothesSprite(0x1BC, MyColors.CYAN);
    private final boolean busty;

    public NarrowShoulders(boolean busty) {
        super(busty);
        this.busty = busty;
    }


    @Override
    public void makeNaked(PortraitSprite[][] grid) {
        grid[0][5] = PortraitSprite.FRAME_LEFT;
        grid[0][6] = PortraitSprite.FRAME_LL_CORNER;

        grid[1][5] = NARROW_SHOULDER_LEFT_TOP;
        if (busty) {
            grid[1][6] = NARROW_SHOULDER_LEFT_BOTTOM_BUSTY;
            grid[5][6] = NARROW_SHOULDER_RIGHT_BOTTOM_BUSTY;
        } else {
            grid[1][6] = NARROW_SHOULDER_LEFT_BOTTOM;
            grid[5][6] = NARROW_SHOULDER_RIGHT_BOTTOM;
        }

        grid[5][5] = NARROW_SHOULDER_RIGHT_TOP;

        grid[6][5] = PortraitSprite.FRAME_RIGHT;
        grid[6][6] = PortraitSprite.FRAME_LR_CORNER;
    }

    public PortraitSprite makeLeftTopSprite(MyColors color) {
        return PortraitSprite.FRAME_LEFT;
    }

    public PortraitSprite makeInnerLeftTopSprite(MyColors color) {
        return new ClothesSprite(0x3D, color);
    }

    public PortraitSprite makeInnerRightTopSprite(MyColors color) {
        return new ClothesSprite(0x3E, color);
    }

    public PortraitSprite makeRightTopSprite(MyColors color) {
        return PortraitSprite.FRAME_RIGHT;
    }

    public PortraitSprite makeLeftBottomSprite(MyColors color) {
        return PortraitSprite.FRAME_LL_CORNER;
    }

    public PortraitSprite makeRightBottomSprite(MyColors color) {
        return PortraitSprite.FRAME_LR_CORNER;
    }

    public PortraitSprite makeInnerLeftBottomSprite(MyColors color) {
        if (busty) {
            return new ClothesSprite(0xD7, color);
        }
        return new ClothesSprite(0x4D, color);
    }

    public PortraitSprite makeInnerRightBottomSprite(MyColors color) {
        if (busty) {
            return new ClothesSprite(0xD8, color);
        }
        return new ClothesSprite(0x4E, color);
    }

    public void putOnHood(CharacterAppearance characterAppearance, MyColors color) {
        characterAppearance.setSprite(1, 5, new ClothesSprite(0x6D, color));
        characterAppearance.setSprite(5, 5, new ClothesSprite(0x6E, color));
    }

    public void putOnLeftPauldron(CharacterAppearance characterAppearance, MyColors armorColor, MyColors underShirtColor) {
        characterAppearance.setSprite(1, 5, new ClothesSprite(0xCB, underShirtColor, armorColor));
        characterAppearance.setSprite(1, 6, new ClothesSprite(0xDB, underShirtColor, armorColor));
        characterAppearance.setSprite(0, 6, new ClothesSprite(0xDA, underShirtColor, armorColor));
    }

    public void putOnRightPauldron(CharacterAppearance characterAppearance, MyColors armorColor, MyColors underShirtColor) {
        characterAppearance.setSprite(5, 5, new ClothesSprite(0xCC, underShirtColor, armorColor));
        characterAppearance.setSprite(5, 6, new ClothesSprite(0xDC, underShirtColor, armorColor));
        characterAppearance.setSprite(6, 6, new ClothesSprite(0xDD, underShirtColor, armorColor));
    }

    public void putOnHideRight(CharacterAppearance characterAppearance, MyColors clothingColor) {
        characterAppearance.setSprite(5, 5, new FaceAndClothesSprite(0x13F, clothingColor));
        characterAppearance.setSprite(5, 6, new FaceAndClothesSprite(0x14F, clothingColor));
    }

    public void putOnHideLeft(CharacterAppearance characterAppearance, MyColors clothingColor) {
        characterAppearance.setSprite(1, 5, new FaceAndClothesSprite(0x11F, clothingColor));
        characterAppearance.setSprite(1, 6, new FaceAndClothesSprite(0x12F, clothingColor));
    }

    public void putOnLightArmor(CharacterAppearance characterAppearance, MyColors armorColor, MyColors shirtColor) {
        characterAppearance.setSprite(1, 5, new ClothesSprite(0xBE, armorColor, shirtColor));
        characterAppearance.setSprite(5, 5, new ClothesSprite(0xBF, armorColor, shirtColor));
        characterAppearance.setSprite(1, 6, new ClothesSprite(0xCE, armorColor, shirtColor));
        characterAppearance.setSprite(5, 6, new ClothesSprite(0xCF, armorColor, shirtColor));
    }

    public void putOnApron(CharacterAppearance characterAppearance, MyColors apronColor, MyColors shirtColor) {
        characterAppearance.setSprite(1, 5, new ClothesSprite(0x9E, shirtColor, apronColor));
        characterAppearance.setSprite(5, 5, new ClothesSprite(0x9F, shirtColor, apronColor));
        characterAppearance.setSprite(1, 6, new ClothesSprite(0xAE, shirtColor, apronColor));
        characterAppearance.setSprite(5, 6, new ClothesSprite(0xAF, shirtColor, apronColor));
    }

    public PortraitSprite makeFancyTopLeft(MyColors color, MyColors detailColor) {
        return new FancyShoulderTopLeft(0x7E, color, detailColor);
    }

    public PortraitSprite makeFancyTopRight(MyColors color, MyColors detailColor) {
        return new FancyShoulderTopRight(0x7F, color, detailColor);
    }

    public PortraitSprite makeFancyBottomLeft(MyColors color, MyColors detailColor) {
        return new FancyBottomLeftLeft(0x8E, color, detailColor);
    }

    public PortraitSprite makeFancyBottomRight(MyColors color, MyColors detailColor) {
        return new FancyBottomRightRight(0x8F, color, detailColor);
    }

    public void makeSkeleton(SkeletonAppearance appearance) {
        appearance.setRow(5, new PortraitSprite[]{PortraitSprite.FRAME_LEFT, SHOULDER_LEFT_TOP, SKELETON_SHOULDER_TOP, NECK, SKELETON_SHOULDER_TOP, SHOULDER_RIGHT_TOP, PortraitSprite.FRAME_RIGHT});
        appearance.setRow(6, new PortraitSprite[]{PortraitSprite.FRAME_LL_CORNER, NARROW_SHOULDER_LEFT, RIBS_LEFT, RIBS_MIDDLE, RIBS_RIGHT, NARROW_SHOULDER_RIGHT, PortraitSprite.FRAME_LR_CORNER});
        // FEATURE: Add case for Slender?
    }

    public void putOnSuspenders(CharacterAppearance characterAppearance, MyColors shirtColor, MyColors susColor) {
        characterAppearance.addSpriteOnTop(1, 5,  new ClothesSprite(0x11A, susColor));
        characterAppearance.addSpriteOnTop(1, 6, new ClothesSprite(0x12A, susColor));

        characterAppearance.addSpriteOnTop(5, 5, new ClothesSprite(0x11B, susColor));
        characterAppearance.addSpriteOnTop(5, 6, new ClothesSprite(0x12B, susColor));
    }

    @Override
    public void putOnSkimpyDressLeft(CharacterAppearance characterAppearance, MyColors baseColor, MyColors detailColor) {
        characterAppearance.setSprite(1, 5, new FaceAndClothesSprite(0x268, baseColor, detailColor));
        characterAppearance.setSprite(1, 6, new FaceAndClothesSprite(0x278, baseColor, detailColor));
        characterAppearance.setSprite(2, 6, new FaceAndClothesSprite(0x279, baseColor, detailColor));
    }

    @Override
    public void putOnSkimpyDressRight(CharacterAppearance characterAppearance, MyColors baseColor, MyColors detailColor) {
        FaceAndClothesSprite ur = new FaceAndClothesSprite(0x268, baseColor, detailColor);
        ur.setFlipHorizontal(true);
        characterAppearance.setSprite(5, 5, ur);
        FaceAndClothesSprite lr = new FaceAndClothesSprite(0x278, baseColor, detailColor);
        lr.setFlipHorizontal(true);
        characterAppearance.setSprite(5, 6, lr);
        FaceAndClothesSprite mid = new FaceAndClothesSprite(0x279, baseColor, detailColor);
        mid.setFlipHorizontal(true);
        characterAppearance.setSprite(4, 6, mid);
    }

    public void putOnFancyDressLeft(CharacterAppearance characterAppearance, MyColors clothesColor, MyColors detailColor) {
        characterAppearance.setSprite(1, 5, new FaceAndClothesSprite(0x26D, clothesColor, detailColor));
        characterAppearance.setSprite(1, 6, new FaceAndClothesSprite(0x27D, clothesColor, detailColor));
        characterAppearance.setSprite(2, 6, new FaceAndClothesSprite(0x27C, clothesColor, detailColor));
    }

    public void putOnFancyDressRight(CharacterAppearance characterAppearance, MyColors clothesColor, MyColors detailColor) {
        FaceAndClothesSprite ur = new FaceAndClothesSprite(0x26D, clothesColor, detailColor);
        ur.setFlipHorizontal(true);
        characterAppearance.setSprite(5, 5, ur);
        FaceAndClothesSprite lr = new FaceAndClothesSprite(0x27D, clothesColor, detailColor);
        lr.setFlipHorizontal(true);
        characterAppearance.setSprite(5, 6, lr);
        FaceAndClothesSprite mid = new FaceAndClothesSprite(0x27C, clothesColor, detailColor);
        mid.setFlipHorizontal(true);
        characterAppearance.setSprite(4, 6, mid);
    }
}
