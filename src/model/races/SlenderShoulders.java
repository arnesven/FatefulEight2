package model.races;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;
import view.sprites.*;

public class SlenderShoulders extends Shoulders {

    private final PortraitSprite SLENDER_SHOULDER_LEFT_TOP = new NakedClothesSprite(0x3F);
    private final PortraitSprite SLENDER_SHOULDER_LEFT_BOTTOM = new NakedClothesSprite(0x4F);
    private final PortraitSprite SLENDER_SHOULDER_RIGHT_TOP = new NakedClothesSprite(0x5F);
    private final PortraitSprite SLENDER_SHOULDER_RIGHT_BOTTOM = new NakedClothesSprite(0x6F);
    private final PortraitSprite BUSTY_SLENDER_ARM_LEFT = new NakedClothesSprite(0xF1);
    private final PortraitSprite BUSTY_SLENDER_ARM_RIGHT = new NakedClothesSprite(0xF2);
    private final PortraitSprite SLENDER_ARM_LEFT = new NakedClothesSprite(0x10E);
    private final PortraitSprite SLENDER_ARM_RIGHT = new NakedClothesSprite(0x10F);
    private final boolean gender;

    public SlenderShoulders(boolean gender) {
        super(gender);
        this.gender = gender;
    }

    @Override
    public void makeNaked(PortraitSprite[][] grid) {
        grid[0][5] = SLENDER_SHOULDER_LEFT_TOP;
        grid[6][5] = SLENDER_SHOULDER_RIGHT_TOP;
        grid[0][6] = SLENDER_SHOULDER_LEFT_BOTTOM;
        grid[6][6] = SLENDER_SHOULDER_RIGHT_BOTTOM;
        grid[1][5] = SHOULDER_TOP;
        grid[5][5] = SHOULDER_TOP;
        if (gender) {
            grid[1][6] = BUSTY_SLENDER_ARM_LEFT;
            grid[5][6] = BUSTY_SLENDER_ARM_RIGHT;
        } else {
            grid[1][6] = SLENDER_ARM_LEFT;
            grid[5][6] = SLENDER_ARM_RIGHT;
        }
    }


    public PortraitSprite makeLeftTopSprite(MyColors color) {
        return new ClothesSprite(0x3F, color);
    }

    @Override
    public PortraitSprite makeLeftBottomSprite(MyColors color) {
        return new ClothesSprite(0x4F, color);
    }

    @Override
    public PortraitSprite makeRightTopSprite(MyColors color) {
        return new ClothesSprite(0x5F, color);
    }

    @Override
    public PortraitSprite makeRightBottomSprite(MyColors color) {
        return new ClothesSprite(0x6F, color);
    }

    public PortraitSprite makeInnerLeftBottomSprite(MyColors color) {
        if (gender) {
            return new ClothesSprite(0xF1, color);
        }
        return new ClothesSprite(0x10E, color);
    }

    public PortraitSprite makeInnerRightBottomSprite(MyColors color) {
        if (gender) {
            return new ClothesSprite(0xF2, color);
        }
        return new ClothesSprite(0x10F, color);
    }

    @Override
    public PortraitSprite makeFancyBottomLeft(MyColors color, MyColors detailColor) {
        return new ClothesSprite(0xF3, color, detailColor);
    }

    @Override
    public PortraitSprite makeFancyBottomRight(MyColors color, MyColors detailColor) {
        return new ClothesSprite(0xF4, color, detailColor);
    }

    @Override
    public void putOnHideLeft(CharacterAppearance characterAppearance, MyColors clothingColor) {
        characterAppearance.setSprite(1, 5, new FaceAndClothesSprite(0xC8, clothingColor));
        characterAppearance.setSprite(1, 6, new FaceAndClothesSprite(0x17F, clothingColor));
    }

    @Override
    public void putOnHideRight(CharacterAppearance characterAppearance, MyColors clothingColor) {
        characterAppearance.setSprite(5, 5, new FaceAndClothesSprite(0xE8, clothingColor));
        characterAppearance.setSprite(5, 6, new FaceAndClothesSprite(0x18F, clothingColor));
    }

    @Override
    public void putOnLeftPauldron(CharacterAppearance characterAppearance, MyColors armorColor, MyColors underShirtColor) {
        characterAppearance.setSprite(0, 6, new ClothesSprite(0x9B, underShirtColor, armorColor));
        characterAppearance.setSprite(0, 5, new ClothesSprite(0x8B, underShirtColor, armorColor));
        characterAppearance.setSprite(1, 6, new ClothesSprite(0xF5, underShirtColor, armorColor));
        characterAppearance.setSprite(1, 5, new ClothesSprite(0x22, underShirtColor, armorColor));
    }

    @Override
    public void putOnRightPauldron(CharacterAppearance characterAppearance, MyColors armorColor, MyColors underShirtColor) {
        characterAppearance.setSprite(5, 6, new ClothesSprite(0xF6, underShirtColor, armorColor));
        characterAppearance.setSprite(6, 6, new ClothesSprite(0xBB, underShirtColor, armorColor));
        characterAppearance.setSprite(5, 5, new ClothesSprite(0x23, underShirtColor, armorColor));
        characterAppearance.setSprite(6, 5, new ClothesSprite(0xAB, underShirtColor, armorColor));
    }

    @Override
    public void putOnApron(CharacterAppearance characterAppearance, MyColors apronColor, MyColors shirtColor) {
        characterAppearance.setSprite(1, 5, new ClothesSprite(0xB6, shirtColor, apronColor));
        characterAppearance.setSprite(5, 5, new ClothesSprite(0xB8, shirtColor, apronColor));
        characterAppearance.setSprite(1, 6, new ClothesSprite(0xC6, shirtColor, apronColor));
        characterAppearance.setSprite(5, 6, new ClothesSprite(0xC8, shirtColor, apronColor));
        characterAppearance.getSprite(1, 6).setColor4(shirtColor);
        characterAppearance.getSprite(5, 6).setColor4(shirtColor);
    }

    public void putOnSuspenders(CharacterAppearance characterAppearance, MyColors shirtColor, MyColors susColor) {
        characterAppearance.addSpriteOnTop(1, 5,  new ClothesSprite(0x118, susColor));
        characterAppearance.addSpriteOnTop(1, 6, new ClothesSprite(0x128, susColor));

        characterAppearance.addSpriteOnTop(5, 5, new ClothesSprite(0x119, susColor));
        characterAppearance.addSpriteOnTop(5, 6, new ClothesSprite(0x129, susColor));
    }
}
