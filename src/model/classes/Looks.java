package model.classes;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;
import view.sprites.*;

public abstract class Looks {
    protected static void putOnTunic(CharacterAppearance characterAppearance, MyColors color) {
        characterAppearance.setRow(5, new PortraitSprite[]{
                new ShoulderLeftTop(color), new ShoulderTop(color), new NeckLeft(color),
                new Neck(color), new NeckRight(color), new ShoulderTop(color), new ShoulderRightTop(color)
        });
        characterAppearance.setRow(6, new PortraitSprite[]{
                new ShoulderLeftBottom(color), new FilledBlockClothes(color), new FilledBlockClothes(color),
                new Chest(color), new FilledBlockClothes(color), new FilledBlockClothes(color),
                new ShoulderRightBottom(color)
        });
    }

    protected static void putOnLooseShirt(CharacterAppearance characterAppearance, MyColors color) {
        characterAppearance.setRow(5, new PortraitSprite[]{
                new ShoulderLeftTop(color), new ShoulderTop(color), new WideNeckLeft(color),
                new Neck(color), new WideNeckRight(color), new ShoulderTop(color), new ShoulderRightTop(color)
        });
        characterAppearance.setRow(6, new PortraitSprite[]{
                new ShoulderLeftBottom(color), new FilledBlockClothes(color), new WideChestLeft(color),
                new WideChest(color), new WideChestRight(color), new FilledBlockClothes(color),
                new ShoulderRightBottom(color)
        });
    }

    protected static void putOnRobe(CharacterAppearance characterAppearance, MyColors color, MyColors detailColor) {
        characterAppearance.setRow(5, new PortraitSprite[]{
                new ShoulderLeftTop(color), new ShoulderTop(color), new FancyNeckLeft(color, detailColor),
                new Neck(color), new FancyNeckRight(color, detailColor), new ShoulderTop(color), new ShoulderRightTop(color)
        });
        characterAppearance.setRow(6, new PortraitSprite[]{
                new ShoulderLeftBottom(color), new FilledBlockClothes(color), new TriangleLeft(color, detailColor),
                new FancyChest(detailColor), new TriangleRight(color, detailColor), new FilledBlockClothes(color),
                new ShoulderRightBottom(color)
        });
    }

    protected static void putOnFancyRobe(CharacterAppearance characterAppearance, MyColors color, MyColors detailColor) {
        characterAppearance.setRow(5, new PortraitSprite[]{
                new ShoulderLeftTop(color), new FancyShoulderTopLeft(color, detailColor), new RaisedNeckLeft(detailColor),
                new Neck(color), new RaisedNeckRight(detailColor), new FancyShoulderTopRight(color, detailColor), new ShoulderRightTop(color)
        });
        characterAppearance.setRow(6, new PortraitSprite[]{
                new ShoulderLeftBottom(color), new FancyBottomLeftLeft(color, detailColor), new FancyBottomLeft(color, detailColor),
                new VeryFancyChest(detailColor), new FancyBottomRight(color, detailColor), new FancyBottomRightRight(color, detailColor),
                new ShoulderRightBottom(color)
        });
    }

    protected static void putOnHood(CharacterAppearance characterAppearance, MyColors color) {
        characterAppearance.removeOuterHair();
        characterAppearance.setRow(1, new PortraitSprite[]{
                new PortraitFrameSprite(PortraitFrameSprite.LEFT), new FilledBlockSprite(MyColors.BLACK),
                new HoodLeftTop(color, characterAppearance), new HoodTop(color, characterAppearance),
                new HoodRightTop(color, characterAppearance), new FilledBlockSprite(MyColors.BLACK),
                new PortraitFrameSprite(PortraitFrameSprite.RIGHT)
        });
        characterAppearance.setSprite(1, 2, new ClothesSprite(0x30, color));
        characterAppearance.setSprite(5, 2, new ClothesSprite(0x31, color));
        characterAppearance.setSprite(1, 3, new ClothesSprite(0x40, color));
        characterAppearance.setSprite(5, 3, new ClothesSprite(0x41, color));
        characterAppearance.setSprite(1, 4, new ClothesSprite(0x50, color));
        characterAppearance.setSprite(5, 4, new ClothesSprite(0x51, color));
        characterAppearance.setSprite(1, 5, new ClothesSprite(0x60, color));
        characterAppearance.setSprite(2, 5, new FaceAndClothesSpriteWithBack(0xE0, color, MyColors.DARK_GRAY));
        characterAppearance.setSprite(3, 5, new FaceAndClothesSprite(0xE1, color));
        characterAppearance.setSprite(4, 5, new FaceAndClothesSpriteWithBack(0xE2, color, MyColors.DARK_GRAY));
        characterAppearance.setSprite(5, 5, new ClothesSprite(0x61, color));
        characterAppearance.setSprite(2, 6, new ClothesSprite(0x80, color));
        characterAppearance.setSprite(3, 6, new ClothesSprite(0x71, color));
        characterAppearance.setSprite(4, 6, new ClothesSprite(0x81, color));
    }

    protected static void putOnArmor(CharacterAppearance characterAppearance, MyColors armorColor, MyColors underShirtColor) {
        // Left pauldron
        characterAppearance.setSprite(0, 6, new ClothesSprite(0x12, underShirtColor, armorColor));
        characterAppearance.setSprite(1, 6, new ClothesSprite(0x32, underShirtColor, armorColor));
        characterAppearance.setSprite(2, 6, new ClothesSprite(0x52, underShirtColor, armorColor));
        characterAppearance.setSprite(0, 5, new ClothesSprite(0x02, underShirtColor, armorColor));
        characterAppearance.setSprite(1, 5, new ClothesSprite(0x22, underShirtColor, armorColor));
        characterAppearance.setSprite(2, 5, new FaceAndClothesSpriteWithBack(0xA1, armorColor, underShirtColor));

        characterAppearance.setSprite(3, 6, new ClothesSprite(0x63, underShirtColor, armorColor));
        characterAppearance.setSprite(3, 5, new FaceAndClothesSpriteWithBack(0x92, armorColor, underShirtColor));

        // Right pauldron
        characterAppearance.setSprite(4, 6, new ClothesSprite(0x53, underShirtColor, armorColor));
        characterAppearance.setSprite(5, 6, new ClothesSprite(0x33, underShirtColor, armorColor));
        characterAppearance.setSprite(6, 6, new ClothesSprite(0x13, underShirtColor, armorColor));
        characterAppearance.setSprite(4, 5, new FaceAndClothesSpriteWithBack(0xB1, armorColor, underShirtColor));
        characterAppearance.setSprite(5, 5, new ClothesSprite(0x23, underShirtColor, armorColor));
        characterAppearance.setSprite(6, 5, new ClothesSprite(0x03, underShirtColor, armorColor));
    }

    protected static void putOnPointyHat(CharacterAppearance characterAppearance, MyColors hatColor) {
        characterAppearance.removeOuterHair();
        characterAppearance.setSprite(2, 0, new ClothesSprite(0x90, hatColor));
        characterAppearance.setSprite(3, 0, new ClothesSprite(0x91, hatColor));
        characterAppearance.setSprite(4, 0, new ClothesSprite(0x92, hatColor));
        characterAppearance.setSprite(1, 1, new ClothesSprite(0xA0, hatColor));
        characterAppearance.setSprite(2, 1, new ClothesSprite(0xA1, hatColor));
        characterAppearance.setSprite(3, 1, new ClothesSprite(0xA2, hatColor));
        characterAppearance.setSprite(4, 1, new ClothesSprite(0xA3, hatColor));
        characterAppearance.setSprite(5, 1, new ClothesSprite(0xA4, hatColor));
        characterAppearance.setRow(2, new PortraitSprite[]{
                new ClothesSprite(0xB0, hatColor), new ClothesSprite(0xB1, hatColor),
                new FilledBlockSprite(hatColor), new FilledBlockSprite(hatColor), new FilledBlockSprite(hatColor),
                new ClothesSprite(0xB2, hatColor), new ClothesSprite(0xB3, hatColor)
        });
    }

    protected static void putOnCap(CharacterAppearance appearance, MyColors color) {
        appearance.removeOuterHair();
        appearance.setSprite(2, 1, new FaceAndClothesSpriteWithBack(0xE4, appearance.getHairColor(), color, MyColors.BEIGE));
        appearance.setSprite(3, 1, new FaceAndClothesSpriteWithBack(0xE5, appearance.getHairColor(), color, MyColors.BEIGE));
        appearance.setSprite(4, 1, new FaceAndClothesSpriteWithBack(0xE6, appearance.getHairColor(), color, MyColors.BEIGE));
        appearance.setSprite(2, 2, new FaceAndClothesSpriteWithBack(0xF4, appearance.getHairColor(), color, MyColors.BEIGE));
        appearance.setSprite(3, 2, new FaceAndClothesSpriteWithBack(0xF5, appearance.getHairColor(), color, MyColors.BEIGE));
        appearance.setSprite(4, 2, new FaceAndClothesSpriteWithBack(0xF6, appearance.getHairColor(), color, MyColors.BEIGE));
    }

    protected static void finalizeCap(CharacterAppearance appearance) {
        if (appearance.hairInForehead()) {
            for (int x = 2; x <= 4; ++x) {
                appearance.getSprite(x, 2).setColor1(appearance.getHairColor());
            }
        }
    }

    public static void putOnHideRight(CharacterAppearance characterAppearance, MyColors clothingColor) {
        characterAppearance.setSprite(5, 5, new FaceAndClothesSprite(0xE8, clothingColor));
        characterAppearance.setSprite(6, 5, new FaceAndClothesSprite(0xE9, clothingColor));
        characterAppearance.setSprite(4, 6, new FaceAndClothesSprite(0xF7, clothingColor));
        characterAppearance.setSprite(5, 6, new FaceAndClothesSprite(0xF8, clothingColor));
        characterAppearance.setSprite(6, 6, new FaceAndClothesSprite(0xF9, clothingColor));
    }

    public static void putOnHideLeft(CharacterAppearance characterAppearance, MyColors clothingColor) {
        characterAppearance.setSprite(0, 5, new FaceAndClothesSprite(0xC7, clothingColor));
        characterAppearance.setSprite(1, 5, new FaceAndClothesSprite(0xC8, clothingColor));
        characterAppearance.setSprite(0, 6, new FaceAndClothesSprite(0xD7, clothingColor));
        characterAppearance.setSprite(1, 6, new FaceAndClothesSprite(0xD8, clothingColor));
        characterAppearance.setSprite(2, 6, new FaceAndClothesSprite(0xD9, clothingColor));
    }

    public static void putOnFancyDress(CharacterAppearance characterAppearance, MyColors baseColor, MyColors detailColor) {
        characterAppearance.setSprite(1, 5, new FaceAndClothesSprite(0x160, baseColor, detailColor));
        characterAppearance.setSprite(1, 6, new FaceAndClothesSprite(0x170, baseColor, detailColor));
        characterAppearance.setSprite(2, 6, new FaceAndClothesSprite(0x171, baseColor, detailColor));
        characterAppearance.setSprite(5, 5, new FaceAndClothesSprite(0x163, baseColor, detailColor));
        characterAppearance.setSprite(5, 6, new FaceAndClothesSprite(0x173, baseColor, detailColor));
        characterAppearance.setSprite(4, 6, new FaceAndClothesSprite(0x172, baseColor, detailColor));
    }

    public static void putOnLightArmor(CharacterAppearance characterAppearance, MyColors armorColor, MyColors shirtColor) {
        characterAppearance.setSprite(1, 5, new ClothesSprite(0x95, armorColor, shirtColor));
        characterAppearance.setSprite(1, 6, new ClothesSprite(0xA5, armorColor, shirtColor));
        characterAppearance.setSprite(2, 6, new ClothesSprite(0xA6, armorColor, shirtColor));
        characterAppearance.setSprite(3, 6, new ClothesSprite(0xA7, armorColor, shirtColor));
        characterAppearance.setSprite(4, 6, new ClothesSprite(0xA8, armorColor, shirtColor));
        characterAppearance.setSprite(5, 5, new ClothesSprite(0x95, armorColor, shirtColor));
        characterAppearance.setSprite(5, 6, new ClothesSprite(0xA5, armorColor, shirtColor));
    }

    protected static void putOnFarmersHat(CharacterAppearance characterAppearance, MyColors hatColor) {
        characterAppearance.removeOuterHair();
        for (int x = 0; x < 7; ++x) {
            characterAppearance.setSprite(x, 1, new ClothesSprite(0xD0 + x, hatColor));
            characterAppearance.setSprite(x, 2, new ClothesSprite(0xE0 + x, hatColor));
        }
    }

    public static void putOnMerchantHat(CharacterAppearance characterAppearance, MyColors hatColor, MyColors detailColor) {
        putOnPointyHat(characterAppearance, hatColor);
        characterAppearance.setSprite(2, 1, new ClothesSprite(0xF0, hatColor, detailColor));
    }

    public static void putOnMask(CharacterAppearance characterAppearance, MyColors clothingColor) {
        characterAppearance.getSprite(2, 2).setColor3(clothingColor);
        characterAppearance.getSprite(4, 2).setColor3(clothingColor);
        characterAppearance.setSprite(2, 4, new FaceAndClothesSpriteWithBack(0x31, clothingColor, MyColors.DARK_GRAY));
        characterAppearance.setSprite(3, 4, new ClothesSprite(0x70, clothingColor));
        characterAppearance.setSprite(4, 4, new FaceAndClothesSpriteWithBack(0x41, clothingColor, MyColors.DARK_GRAY));
        characterAppearance.setSprite(3, 5, new FaceAndClothesSprite(0x91, clothingColor));
    }
}
