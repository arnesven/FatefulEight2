package model.races;

import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.SkeletonAppearance;
import view.MyColors;
import view.sprites.*;

import java.io.Serializable;

public abstract class Shoulders implements Serializable {

    protected final PortraitSprite SHOULDER_TOP = new NakedClothesSprite(0x20);
    protected final PortraitSprite FILLED_BLOCK_CLOTHES = new NakedClothesSprite(0xFF);
    protected final PortraitSprite SHOULDER_LEFT_BOTTOM = new NakedClothesSprite(0x10);
    protected final PortraitSprite SHOULDER_RIGHT_BOTTOM = new NakedClothesSprite(0x11);

    protected static final PortraitSprite SKELETON_SHOULDER_TOP = new FaceAndClothesSprite(0x18B, MyColors.CYAN);
    protected static final PortraitSprite NECK = new FaceAndClothesSprite(0x18C, MyColors.CYAN);

    private static final PortraitSprite SHOULDER_LEFT_TOP_WITH_FRAME = new FaceAndClothesSprite(0x19A, MyColors.CYAN);
    public static final PortraitSprite RIBS_EXTENSION = new FaceAndClothesSprite(0x19C, MyColors.CYAN);
    private static final PortraitSprite SHOULDER_RIGHT_TOP_WITH_FRAME = new FaceAndClothesSprite(0x19E, MyColors.CYAN);

    private static final PortraitSprite LL_CORNER = new FaceAndClothesSprite(0x1AA, MyColors.CYAN);
    public static final PortraitSprite RIBS_LEFT = new FaceAndClothesSprite(0x1AB, MyColors.CYAN);
    public static final PortraitSprite RIBS_MIDDLE = new FaceAndClothesSprite(0x1AC, MyColors.CYAN);
    public static final PortraitSprite RIBS_RIGHT = new FaceAndClothesSprite(0x1AD, MyColors.CYAN);
    private static final PortraitSprite LR_CORNER = new FaceAndClothesSprite(0x1AE, MyColors.CYAN);



    public abstract void makeNaked(PortraitSprite[][] grid);

    public PortraitSprite makeLeftTopSprite(MyColors color) {
        return new ShoulderLeftTop(color);
    }

    public PortraitSprite makeInnerLeftTopSprite(MyColors color) {
        return new ShoulderTop(color);
    }

    public PortraitSprite makeInnerRightTopSprite(MyColors color) {
        return new ShoulderTop(color);
    }

    public PortraitSprite makeRightTopSprite(MyColors color) {
        return new ShoulderRightTop(color);
    }

    public PortraitSprite makeLeftBottomSprite(MyColors color) {
        return new ShoulderLeftBottom(color);
    }

    public PortraitSprite makeRightBottomSprite(MyColors color) {
        return new ShoulderRightBottom(color);
    }

    public PortraitSprite makeInnerLeftBottomSprite(MyColors color) {
        return new FilledBlockClothes(color);
    }

    public PortraitSprite makeInnerRightBottomSprite(MyColors color) {
        return new FilledBlockClothes(color);
    }

    public void putOnHood(CharacterAppearance characterAppearance, MyColors color) {
        characterAppearance.setSprite(1, 5, new ClothesSprite(0x60, color));
        characterAppearance.setSprite(5, 5, new ClothesSprite(0x61, color));
    }

    public void putOnLeftPauldron(CharacterAppearance characterAppearance, MyColors armorColor, MyColors underShirtColor) {
        characterAppearance.setSprite(0, 6, new ClothesSprite(0x12, underShirtColor, armorColor));
        characterAppearance.setSprite(0, 5, new ClothesSprite(0x02, underShirtColor, armorColor));
        characterAppearance.setSprite(1, 6, new ClothesSprite(0x32, underShirtColor, armorColor));
        characterAppearance.setSprite(1, 5, new ClothesSprite(0x22, underShirtColor, armorColor));
    }

    public void putOnRightPauldron(CharacterAppearance characterAppearance, MyColors armorColor, MyColors underShirtColor) {
        characterAppearance.setSprite(5, 6, new ClothesSprite(0x33, underShirtColor, armorColor));
        characterAppearance.setSprite(6, 6, new ClothesSprite(0x13, underShirtColor, armorColor));
        characterAppearance.setSprite(5, 5, new ClothesSprite(0x23, underShirtColor, armorColor));
        characterAppearance.setSprite(6, 5, new ClothesSprite(0x03, underShirtColor, armorColor));
    }

    public void putOnHideRight(CharacterAppearance characterAppearance, MyColors clothingColor) {
        characterAppearance.setSprite(5, 5, new FaceAndClothesSprite(0xE8, clothingColor));
        characterAppearance.setSprite(6, 5, new FaceAndClothesSprite(0xE9, clothingColor));
        characterAppearance.setSprite(5, 6, new FaceAndClothesSprite(0xF8, clothingColor));
        characterAppearance.setSprite(6, 6, new FaceAndClothesSprite(0xF9, clothingColor));
    }

    public void putOnHideLeft(CharacterAppearance characterAppearance, MyColors clothingColor) {
        characterAppearance.setSprite(0, 5, new FaceAndClothesSprite(0xC7, clothingColor));
        characterAppearance.setSprite(1, 5, new FaceAndClothesSprite(0xC8, clothingColor));
        characterAppearance.setSprite(0, 6, new FaceAndClothesSprite(0xD7, clothingColor));
        characterAppearance.setSprite(1, 6, new FaceAndClothesSprite(0xD8, clothingColor));
    }

    public void putOnLightArmor(CharacterAppearance characterAppearance, MyColors armorColor, MyColors shirtColor) {
        characterAppearance.setSprite(1, 5, new ClothesSprite(0x95, armorColor, shirtColor));
        characterAppearance.setSprite(5, 5, new ClothesSprite(0x95, armorColor, shirtColor));
        characterAppearance.setSprite(1, 6, new ClothesSprite(0xA5, armorColor, shirtColor));
        characterAppearance.setSprite(5, 6, new ClothesSprite(0xA5, armorColor, shirtColor));
    }

    public void putOnApron(CharacterAppearance characterAppearance, MyColors apronColor, MyColors shirtColor) {
        characterAppearance.setSprite(1, 5, new ClothesSprite(0xB6, shirtColor, apronColor));
        characterAppearance.setSprite(5, 5, new ClothesSprite(0xB8, shirtColor, apronColor));
        characterAppearance.setSprite(1, 6, new ClothesSprite(0xC6, shirtColor, apronColor));
        characterAppearance.setSprite(5, 6, new ClothesSprite(0xC8, shirtColor, apronColor));
    }

    public PortraitSprite makeFancyTopLeft(MyColors color, MyColors detailColor) {
        return new FancyShoulderTopLeft(0xB4, color, detailColor);
    }

    public PortraitSprite makeFancyTopRight(MyColors color, MyColors detailColor) {
        return new FancyShoulderTopRight(0xB5, color, detailColor);
    }

    public PortraitSprite makeFancyBottomLeft(MyColors color, MyColors detailColor) {
        return new FancyBottomLeftLeft(0xC2, color, detailColor);
    }

    public PortraitSprite makeFancyBottomRight(MyColors color, MyColors detailColor) {
        return new FancyBottomRightRight(0xC5, color, detailColor);
    }

    public void makeSkeleton(SkeletonAppearance appearance) {
         // TODO: Add case for Slender?
        appearance.setRow(5, new PortraitSprite[]{SHOULDER_LEFT_TOP_WITH_FRAME, SKELETON_SHOULDER_TOP, SKELETON_SHOULDER_TOP, NECK, SKELETON_SHOULDER_TOP, SKELETON_SHOULDER_TOP, SHOULDER_RIGHT_TOP_WITH_FRAME});
        appearance.setRow(6, new PortraitSprite[]{LL_CORNER, RIBS_LEFT, RIBS_EXTENSION, RIBS_MIDDLE, RIBS_EXTENSION, RIBS_RIGHT, LR_CORNER});
    }
}

