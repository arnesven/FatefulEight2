package model.characters.appearance;

import view.MyColors;
import view.sprites.*;

public class SkeletonNeck implements TorsoNeck {
    @Override
    public void makeNaked(PortraitSprite[][] grid) {
        grid[3][5] = makeCenter(MyColors.WHITE);
    }

    @Override
    public PortraitSprite makeCenter(MyColors skinColor) {
        return new FaceAndClothesSprite(0x18C, skinColor);
    }

    @Override
    public PortraitSprite makeLeft(MyColors skinColor) {
        return new FaceAndClothesSprite(0x1A0, skinColor);
    }

    @Override
    public PortraitSprite makeRight(MyColors skinColor) {
        return new FaceAndClothesSprite(0x1B0, skinColor);
    }

    @Override
    public PortraitSprite getHoodLeft(MyColors color) {
        return new FaceAndClothesSpriteWithBack(0x1A5, color, MyColors.DARK_GRAY);
    }

    @Override
    public PortraitSprite getHoodRight(MyColors color) {
        return new FaceAndClothesSpriteWithBack(0x1B5, color, MyColors.DARK_GRAY);
    }

    @Override
    public PortraitSprite getArmorLeft(MyColors armorColor, MyColors underShirtColor) {
        return new FaceAndClothesSpriteWithBack(0x1A1, armorColor, underShirtColor);
    }

    @Override
    public PortraitSprite getArmorRight(MyColors armorColor, MyColors underShirtColor) {
        return new FaceAndClothesSpriteWithBack(0x1B1, armorColor, underShirtColor);
    }

    @Override
    public PortraitSprite makeFancyLeft(MyColors color, MyColors detailColor) {
        return new FancyNeckLeft(0x1A2, color, detailColor);
    }

    @Override
    public PortraitSprite makeFancyRight(MyColors color, MyColors detailColor) {
        return new FancyNeckRight(0x1B2, color, detailColor);
    }

    @Override
    public PortraitSprite makeWideLeft(MyColors color) {
        return new FaceAndClothesSprite(0x1A3, color);
    }

    @Override
    public PortraitSprite makeWideRight(MyColors color) {
        return new FaceAndClothesSprite(0x1B3, color);
    }

    @Override
    public PortraitSprite makeRaisedLeft(MyColors detailColor) {
        return new FaceAndClothesSprite(0x1A4, detailColor);
    }

    @Override
    public PortraitSprite makeRaisedRight(MyColors detailColor) {
        return new FaceAndClothesSprite(0x1B4, detailColor);
    }
}
