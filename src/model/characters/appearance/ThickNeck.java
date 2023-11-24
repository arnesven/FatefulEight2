package model.characters.appearance;

import view.MyColors;
import view.sprites.*;

public class ThickNeck implements TorsoNeck {
    private final PortraitSprite NECK_1 = new NakedFaceAndClothesSprite(0x90);

    private final PortraitSprite NECK_LEFT = new NakedFaceAndClothesSprite(0x1C0);
    private final PortraitSprite NECK_RIGHT = new NakedFaceAndClothesSprite(0x1D0);

    @Override
    public void makeNaked(PortraitSprite[][] grid) {
        grid[2][5] = NECK_LEFT;
        grid[3][5] = NECK_1;
        grid[4][5] = NECK_RIGHT;
    }

    @Override
    public PortraitSprite makeCenter(MyColors colors) {
        return new Neck(colors);
    }

    @Override
    public PortraitSprite makeLeft(MyColors skinColor) {
        return new FaceAndClothesSprite(0x1C0, skinColor);
    }

    @Override
    public PortraitSprite makeRight(MyColors skinColor) {
        return new FaceAndClothesSprite(0x1D0, skinColor);
    }

    @Override
    public PortraitSprite getHoodLeft(MyColors color) {
        return new FaceAndClothesSpriteWithBack(0x1D5, color, MyColors.DARK_GRAY);
    }

    @Override
    public PortraitSprite getHoodRight(MyColors color) {
        return new FaceAndClothesSpriteWithBack(0x1D6, color, MyColors.DARK_GRAY);
    }

    @Override
    public PortraitSprite getArmorLeft(MyColors armorColor, MyColors underShirtColor) {
        return new FaceAndClothesSpriteWithBack(0x1C1, armorColor, underShirtColor);
    }

    @Override
    public PortraitSprite getArmorRight(MyColors armorColor, MyColors underShirtColor) {
        return new FaceAndClothesSpriteWithBack(0x1D1, armorColor, underShirtColor);
    }

    @Override
    public PortraitSprite makeFancyLeft(MyColors color, MyColors detailColor) {
        return new FancyNeckLeft(0x1C2, color, detailColor);
    }

    @Override
    public PortraitSprite makeFancyRight(MyColors color, MyColors detailColor) {
        return new FancyNeckRight(0x1D2, color, detailColor);
    }

    @Override
    public PortraitSprite makeWideLeft(MyColors color) {
        return new FaceAndClothesSprite(0x1C3, color);
    }

    @Override
    public PortraitSprite makeWideRight(MyColors color) {
        return new FaceAndClothesSprite(0x1D3, color);
    }

    @Override
    public PortraitSprite makeRaisedLeft(MyColors detailColor) {
        return new FaceAndClothesSprite(0x1C4, detailColor);
    }

    @Override
    public PortraitSprite makeRaisedRight(MyColors detailColor) {
        return new FaceAndClothesSprite(0x1D4, detailColor);
    }
}
