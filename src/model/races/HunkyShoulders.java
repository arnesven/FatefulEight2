package model.races;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;
import view.sprites.*;

public class HunkyShoulders extends BroadShoulders {

    protected final PortraitSprite SHOULDER_TOP_LEFT = new NakedClothesSprite(0x100);
    protected final PortraitSprite SHOULDER_TOP_RIGHT = new NakedClothesSprite(0x101);

    public HunkyShoulders(boolean gender) {
        super(gender);
    }

    @Override
    public void makeNaked(PortraitSprite[][] grid) {
        super.makeNaked(grid);
        grid[1][5] = SHOULDER_TOP_LEFT;
        grid[5][5] = SHOULDER_TOP_RIGHT;
    }

    @Override
    public PortraitSprite makeInnerLeftTopSprite(MyColors color) {
        return new ClothesSprite(0x100, color);
    }

    @Override
    public PortraitSprite makeInnerRightTopSprite(MyColors color) {
        return new ClothesSprite(0x101, color);
    }

    @Override
    public PortraitSprite makeFancyTopLeft(MyColors color, MyColors detailColor) {
        return new FancyShoulderTopLeft(0x102, color, detailColor);
    }

    @Override
    public PortraitSprite makeFancyTopRight(MyColors color, MyColors detailColor) {
        return new FancyShoulderTopLeft(0x103, color, detailColor);
    }

    public void putOnLightArmor(CharacterAppearance characterAppearance, MyColors armorColor, MyColors shirtColor) {
        super.putOnLightArmor(characterAppearance, armorColor, shirtColor);
        characterAppearance.setSprite(1, 5, new ClothesSprite(0x104, armorColor, shirtColor));
        characterAppearance.setSprite(5, 5, new ClothesSprite(0x105, armorColor, shirtColor));
    }

    public void putOnLeftPauldron(CharacterAppearance characterAppearance, MyColors armorColor, MyColors underShirtColor) {
        super.putOnLeftPauldron(characterAppearance, armorColor, underShirtColor);
        characterAppearance.setSprite(1, 5, new ClothesSprite(0x106, underShirtColor, armorColor));
    }

    public void putOnRightPauldron(CharacterAppearance characterAppearance, MyColors armorColor, MyColors underShirtColor) {
        super.putOnRightPauldron(characterAppearance, armorColor, underShirtColor);
        characterAppearance.setSprite(5, 5, new ClothesSprite(0x107, underShirtColor, armorColor));
    }

    public void putOnApron(CharacterAppearance characterAppearance, MyColors apronColor, MyColors shirtColor) {
        super.putOnApron(characterAppearance, apronColor, shirtColor);
        characterAppearance.setSprite(1, 5, new ClothesSprite(0x108, shirtColor, apronColor));
        characterAppearance.setSprite(5, 5, new ClothesSprite(0x109, shirtColor, apronColor));
    }

    public void putOnHideRight(CharacterAppearance characterAppearance, MyColors clothingColor) {
        super.putOnHideRight(characterAppearance, clothingColor);
        characterAppearance.setSprite(5, 5, new FaceAndClothesSprite(0x1D8, clothingColor));
    }

    public void putOnHideLeft(CharacterAppearance characterAppearance, MyColors clothingColor) {
        super.putOnHideLeft(characterAppearance, clothingColor);
        characterAppearance.setSprite(1, 5, new FaceAndClothesSprite(0x1D7, clothingColor));
    }

    public void putOnSuspenders(CharacterAppearance characterAppearance, MyColors shirtColor, MyColors susColor) {
        characterAppearance.addSpriteOnTop(1, 5,  new ClothesSprite(0x115, susColor));
        characterAppearance.addSpriteOnTop(1, 6, new ClothesSprite(0x125, susColor));

        characterAppearance.addSpriteOnTop(5, 5, new ClothesSprite(0x116, susColor));
        characterAppearance.addSpriteOnTop(5, 6, new ClothesSprite(0x126, susColor));
    }

    public void putOnNecklaceTop(CharacterAppearance characterAppearance) {
        characterAppearance.addSpriteOnTop(2, 5, new ClothesSprite(0x11D, MyColors.BLACK));
        characterAppearance.addSpriteOnTop(4, 5, new ClothesSprite(0x11F, MyColors.BLACK));
    }
}
