package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.TorsoNeck;
import model.races.Race;
import model.races.Shoulders;
import view.MyColors;
import view.sprites.*;

public abstract class Looks {
    public static void putOnTunic(CharacterAppearance characterAppearance, MyColors color) {
        Race race = characterAppearance.getRace();
        Shoulders shoulders = characterAppearance.getShoulders();
        TorsoNeck neck = characterAppearance.getNeck();
        characterAppearance.setRow(5, new PortraitSprite[]{
                shoulders.makeLeftTopSprite(color), shoulders.makeInnerLeftTopSprite(color), neck.makeLeft(color),
                neck.makeCenter(race.getColor()), neck.makeRight(color),
                shoulders.makeInnerRightTopSprite(color), shoulders.makeRightTopSprite(color)
        });
        characterAppearance.setRow(6, new PortraitSprite[]{
                shoulders.makeLeftBottomSprite(color), shoulders.makeInnerLeftBottomSprite(color), new FilledBlockClothes(color),
                characterAppearance.getChest().getTunicSprite(color), new FilledBlockClothes(color), shoulders.makeInnerRightBottomSprite(color),
                shoulders.makeRightBottomSprite(color)
        });
    }

    public static void putOnLooseShirt(CharacterAppearance characterAppearance, MyColors color) {
        Shoulders shoulders = characterAppearance.getShoulders();
        Race race = characterAppearance.getRace();
        TorsoNeck neck = characterAppearance.getNeck();
        characterAppearance.setRow(5, new PortraitSprite[]{
                shoulders.makeLeftTopSprite(color), shoulders.makeInnerLeftTopSprite(color), neck.makeWideLeft(color),
                neck.makeCenter(color), neck.makeWideRight(color), shoulders.makeInnerRightTopSprite(color), shoulders.makeRightTopSprite(color)
        });
        characterAppearance.setRow(6, new PortraitSprite[]{
                shoulders.makeLeftBottomSprite(color), shoulders.makeInnerLeftBottomSprite(color), new WideChestLeft(color),
                characterAppearance.getChest().getLooseShirtSprite(color), new WideChestRight(color), shoulders.makeInnerRightBottomSprite(color),
                shoulders.makeRightBottomSprite(color)
        });
    }

    public static void putOnRobe(CharacterAppearance characterAppearance, MyColors color, MyColors detailColor) {
        Shoulders shoulders = characterAppearance.getShoulders();
        Race race = characterAppearance.getRace();
        TorsoNeck neck = characterAppearance.getNeck();
        characterAppearance.setRow(5, new PortraitSprite[]{
                shoulders.makeLeftTopSprite(color), shoulders.makeInnerLeftTopSprite(color), neck.makeFancyLeft(color, detailColor),
                neck.makeCenter(color), neck.makeFancyRight(color, detailColor), shoulders.makeInnerRightTopSprite(color), shoulders.makeRightTopSprite(color)
        });
        characterAppearance.setRow(6, new PortraitSprite[]{
                shoulders.makeLeftBottomSprite(color), shoulders.makeInnerLeftBottomSprite(color), new TriangleLeft(color, detailColor),
                characterAppearance.getChest().getFancySprite(detailColor), new TriangleRight(color, detailColor), shoulders.makeInnerRightBottomSprite(color),
                shoulders.makeRightBottomSprite(color)
        });
    }

    public static void putOnFancyRobe(CharacterAppearance characterAppearance, MyColors color, MyColors detailColor) {
        Shoulders shoulders = characterAppearance.getShoulders();
        Race race = characterAppearance.getRace();
        TorsoNeck neck = characterAppearance.getNeck();
        characterAppearance.setRow(5, new PortraitSprite[]{
                shoulders.makeLeftTopSprite(color), shoulders.makeFancyTopLeft(color, detailColor), neck.makeRaisedLeft(detailColor),
                neck.makeCenter(color), neck.makeRaisedRight(detailColor), shoulders.makeFancyTopRight(color, detailColor), shoulders.makeRightTopSprite(color)
        });
        characterAppearance.setRow(6, new PortraitSprite[]{
                shoulders.makeLeftBottomSprite(color), shoulders.makeFancyBottomLeft(color, detailColor), new FancyBottomLeft(color, detailColor),
                new VeryFancyChest(detailColor), new FancyBottomRight(color, detailColor), shoulders.makeFancyBottomRight(color, detailColor),
                shoulders.makeRightBottomSprite(color)
        });
    }

    public static void putOnHood(CharacterAppearance characterAppearance, MyColors color) {
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
        characterAppearance.getShoulders().putOnHood(characterAppearance, color);

        characterAppearance.setSprite(2, 5, characterAppearance.getNeck().getHoodLeft(color));
        characterAppearance.setSprite(4, 5, characterAppearance.getNeck().getHoodRight(color));

        if (characterAppearance.getRace().isSkeleton()) {
            characterAppearance.setSprite(3, 5, new FaceAndClothesSpriteWithBack(0x1A6, color, MyColors.DARK_GRAY));
        } else {
            characterAppearance.setSprite(3, 5, new FaceAndClothesSprite(0xE1, color));
        }

        characterAppearance.setSprite(2, 6, new ClothesSprite(0x80, color));
        characterAppearance.setSprite(3, 6, new ClothesSprite(0x71, color));
        characterAppearance.setSprite(4, 6, new ClothesSprite(0x81, color));
    }

    public static void putOnArmor(CharacterAppearance characterAppearance, MyColors armorColor, MyColors underShirtColor) {
        // Left pauldron
        characterAppearance.getShoulders().putOnLeftPauldron(characterAppearance, armorColor, underShirtColor);
        characterAppearance.setSprite(2, 6, new ClothesSprite(0x52, underShirtColor, armorColor));

        characterAppearance.setSprite(2, 5, characterAppearance.getNeck().getArmorLeft(armorColor, underShirtColor));
        characterAppearance.setSprite(4, 5, characterAppearance.getNeck().getArmorRight(armorColor, underShirtColor));

        characterAppearance.setSprite(3, 6, new ClothesSprite(0x63, underShirtColor, armorColor));
        if (characterAppearance.getRace().isSkeleton()) {
            characterAppearance.setSprite(3, 5, new FaceAndClothesSpriteWithBack(0x1A8, armorColor, underShirtColor));
        } else {
            characterAppearance.setSprite(3, 5, new FaceAndClothesSpriteWithBack(0x92, armorColor, underShirtColor));
        }

        // Right pauldron
        characterAppearance.setSprite(4, 6, new ClothesSprite(0x53, underShirtColor, armorColor));
        characterAppearance.getShoulders().putOnRightPauldron(characterAppearance, armorColor, underShirtColor);
    }

    public static void putOnPointyHat(CharacterAppearance characterAppearance, MyColors hatColor,
                                         MyColors beltColor, MyColors buckleColor) {
        characterAppearance.removeOuterHair();
        characterAppearance.setSprite(2, 0, new ClothesSprite(0x90, hatColor));
        characterAppearance.setSprite(3, 0, new ClothesSprite(0x91, hatColor));
        characterAppearance.setSprite(4, 0, new ClothesSprite(0x92, hatColor));
        characterAppearance.setSprite(1, 1, new ClothesSprite(0xA0, hatColor));
        characterAppearance.setSprite(2, 1, new ClothesSprite(0xA1, hatColor, beltColor, buckleColor));
        characterAppearance.setSprite(3, 1, new ClothesSprite(0xA2, hatColor, beltColor, buckleColor));
        characterAppearance.setSprite(4, 1, new ClothesSprite(0xA3, hatColor, beltColor, buckleColor));
        characterAppearance.setSprite(5, 1, new ClothesSprite(0xA4, hatColor));
        characterAppearance.setRow(2, new PortraitSprite[]{
                new ClothesSprite(0xB0, hatColor), new ClothesSprite(0xB1, hatColor),
                new FilledBlockSprite(hatColor), new FilledBlockSprite(hatColor), new FilledBlockSprite(hatColor),
                new ClothesSprite(0xB2, hatColor), new ClothesSprite(0xB3, hatColor)
        });
    }

    public static void putOnCap(CharacterAppearance appearance, MyColors color) {
        appearance.removeOuterHair();
        appearance.setSprite(2, 1, new FaceAndClothesSpriteWithBack(0xE4, appearance.getHairColor(), color, MyColors.BEIGE));
        appearance.setSprite(3, 1, new FaceAndClothesSpriteWithBack(0xE5, appearance.getHairColor(), color, MyColors.BEIGE));
        appearance.setSprite(4, 1, new FaceAndClothesSpriteWithBack(0xE6, appearance.getHairColor(), color, MyColors.BEIGE));
        appearance.setSprite(2, 2, new FaceAndClothesSpriteWithBack(0xF4, appearance.getHairColor(), color, MyColors.BEIGE));
        appearance.setSprite(3, 2, new FaceAndClothesSpriteWithBack(0xF5, appearance.getHairColor(), color, MyColors.BEIGE));
        appearance.setSprite(4, 2, new FaceAndClothesSpriteWithBack(0xF6, appearance.getHairColor(), color, MyColors.BEIGE));
    }


    public static void putOnBowlersHat(CharacterAppearance appearance, MyColors fillColor) {
        appearance.removeOuterHair();
        for (int y = 0; y < 3; ++y) {
            for (int x = 1; x < 6; ++x) {
                MyColors color2 = y == 2 ? appearance.getHairColor() : fillColor;
                PortraitSprite spr =  new FaceAndClothesSprite(0x1E4 + 0x10 * y + x, color2, fillColor);
                appearance.setSprite(x, y, spr);
            }
        }
    }

    public static void finalizeCap(CharacterAppearance appearance) {
        if (appearance.hairInForehead()) {
            for (int x = 2; x <= 4; ++x) {
                appearance.getSprite(x, 2).setColor1(appearance.getHairColor());
            }
        }
    }

    public static void putOnHideRight(CharacterAppearance characterAppearance, MyColors clothingColor) {
        if (characterAppearance.getRace().isSkeleton()) {
            return;
        }
        characterAppearance.getShoulders().putOnHideRight(characterAppearance, clothingColor);
        characterAppearance.setSprite(4, 6, new FaceAndClothesSprite(0xF7, clothingColor));
        if (!characterAppearance.getGender()) {
            characterAppearance.getSprite(5, 6).setColor4(characterAppearance.getRace().getColor());
        }
    }

    public static void putOnHideLeft(CharacterAppearance characterAppearance, MyColors clothingColor) {
        if (characterAppearance.getRace().isSkeleton()) {
            return;
        }
        characterAppearance.getShoulders().putOnHideLeft(characterAppearance, clothingColor);
        characterAppearance.setSprite(2, 6, new FaceAndClothesSprite(0xD9, clothingColor));
        if (!characterAppearance.getGender()) {
            characterAppearance.getSprite(1, 6).setColor4(characterAppearance.getRace().getColor());
        }
    }

    public static void putOnSkimpyDress(CharacterAppearance characterAppearance, MyColors baseColor, MyColors detailColor) {
        if (characterAppearance.getRace().isSkeleton()) {
            return;
        }
        characterAppearance.getShoulders().putOnSkimpyDressLeft(characterAppearance, baseColor, detailColor);
        characterAppearance.getShoulders().putOnSkimpyDressRight(characterAppearance, baseColor, detailColor);
    }

    public static void putOnLightArmor(CharacterAppearance characterAppearance, MyColors armorColor, MyColors shirtColor) {
        characterAppearance.getShoulders().putOnLightArmor(characterAppearance, armorColor, shirtColor);

        characterAppearance.setSprite(2, 6, new ClothesSprite(0xA6, armorColor, shirtColor));
        characterAppearance.setSprite(3, 6, new ClothesSprite(0xA7, armorColor, shirtColor));
        characterAppearance.setSprite(4, 6, new ClothesSprite(0xA8, armorColor, shirtColor));
    }

    public static void putOnFarmersHat(CharacterAppearance characterAppearance, MyColors hatColor) {
        characterAppearance.removeOuterHair();
        for (int x = 0; x < 7; ++x) {
            characterAppearance.setSprite(x, 1, new ClothesSprite(0xD0 + x, hatColor));
            characterAppearance.setSprite(x, 2, new ClothesSprite(0xE0 + x, hatColor));
        }
    }

    public static void putOnMerchantHat(CharacterAppearance characterAppearance, MyColors hatColor, MyColors detailColor) {
        putOnPointyHat(characterAppearance, hatColor, hatColor, hatColor);
        characterAppearance.setSprite(2, 1, new ClothesSprite(0xF0, hatColor, hatColor, detailColor));
    }

    public static void putOnMask(CharacterAppearance characterAppearance, MyColors clothingColor) {
        characterAppearance.getSprite(2, 2).setColor3(clothingColor);
        characterAppearance.getSprite(4, 2).setColor3(clothingColor);
        if (characterAppearance.getRace().isSkeleton()) {
            characterAppearance.setSprite(2, 4, new FaceAndClothesSpriteWithBack(0x1A7, clothingColor, MyColors.DARK_GRAY));
            characterAppearance.setSprite(4, 4, new FaceAndClothesSpriteWithBack(0x1B7, clothingColor, MyColors.DARK_GRAY));
            characterAppearance.setSprite(3, 5, new FaceAndClothesSpriteWithBack(0x1B6, clothingColor, MyColors.DARK_GRAY));
        } else {
            characterAppearance.setSprite(2, 4, new FaceAndClothesSpriteWithBack(0x31, clothingColor, MyColors.DARK_GRAY));
            characterAppearance.setSprite(4, 4, new FaceAndClothesSpriteWithBack(0x41, clothingColor, MyColors.DARK_GRAY));
            characterAppearance.setSprite(3, 5, new FaceAndClothesSprite(0x91, clothingColor));
        }
        characterAppearance.setSprite(3, 4, new ClothesSprite(0x70, clothingColor));
    }

    public static void putOnApron(CharacterAppearance characterAppearance, MyColors apronColor, MyColors shirtColor) {
        characterAppearance.setSprite(2, 6, new ClothesSprite(0xC7, shirtColor, apronColor));
        characterAppearance.setSprite(3, 6, new ClothesSprite(0xC7, shirtColor, apronColor));
        characterAppearance.setSprite(4, 6, new ClothesSprite(0xC7, shirtColor, apronColor));

        characterAppearance.getShoulders().putOnApron(characterAppearance, apronColor, shirtColor);
    }

    public static void putOnFancyHat(CharacterAppearance characterAppearance, MyColors lineColor, MyColors fillColor, MyColors featherColor) {
        characterAppearance.removeOuterHair();
        for (int y = 0; y <= 2; ++y) {
            for (int x = 1; x <= 5; ++x) {
                ClothesSprite spr = new ClothesSprite(0x110 + 0x10 * y + x-1, lineColor, fillColor);
                spr.setColor4(featherColor);
                characterAppearance.setSprite(x, y, spr);
            }
        }
    }

    public static void putOnSuspenders(CharacterAppearance characterAppearance, MyColors shirtColor, MyColors susColor) {
        characterAppearance.getShoulders().putOnSuspenders(characterAppearance, shirtColor, susColor);
    }

    public static void putOnNecklace(CharacterAppearance characterAppearance) {
        for (int i = 0; i < 3; ++i) {
            Sprite8x8 spr = new Sprite8x8("necklace", "clothes.png", 0x12C +i);
            spr.setColor1(MyColors.BLACK);
            spr.setColor3(MyColors.BEIGE);
            characterAppearance.addSpriteOnTop(2 + i, 6, spr);
        }
        characterAppearance.getShoulders().putOnNecklaceTop(characterAppearance);
    }
}
