package model.characters.appearance;

import view.MyColors;
import view.sprites.*;

public class NormalNeck implements TorsoNeck {

    private final PortraitSprite NECK_1 = new NakedFaceAndClothesSprite(0x90);

    private final PortraitSprite NECK_LEFT = new NakedFaceAndClothesSprite(0x180);
    private final PortraitSprite NECK_RIGHT = new NakedFaceAndClothesSprite(0x190);

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
    public PortraitSprite makeLeft(MyColors colors) {
        return new NeckLeft(colors);
    }

    @Override
    public PortraitSprite makeRight(MyColors colors) {
        return new NeckRight(colors);
    }

    @Override
    public PortraitSprite getHoodLeft(MyColors color) {
        return new FaceAndClothesSpriteWithBack(0x185, color, MyColors.DARK_GRAY);
    }

    @Override
    public PortraitSprite getHoodRight(MyColors color) {
        return new FaceAndClothesSpriteWithBack(0x195, color, MyColors.DARK_GRAY);
    }

    @Override
    public PortraitSprite getArmorLeft(MyColors armorColor, MyColors underShirtColor) {
        return new FaceAndClothesSpriteWithBack(0x181, armorColor, underShirtColor);
    }

    @Override
    public PortraitSprite getArmorRight(MyColors armorColor, MyColors underShirtColor) {
        return new FaceAndClothesSpriteWithBack(0x191, armorColor, underShirtColor);
    }

    @Override
    public PortraitSprite makeFancyLeft(MyColors color, MyColors detailColor) {
        return new FancyNeckLeft(0x182, color, detailColor);
    }

    @Override
    public PortraitSprite makeFancyRight(MyColors color, MyColors detailColor) {
        return new FancyNeckRight(0x192, color, detailColor);
    }

    @Override
    public PortraitSprite makeWideLeft(MyColors color) {
        return new FaceAndClothesSprite(0x183, color);
    }

    @Override
    public PortraitSprite makeWideRight(MyColors color) {
        return new FaceAndClothesSprite(0x193, color);
    }

    @Override
    public PortraitSprite makeRaisedLeft(MyColors detailColor) {
        return new FaceAndClothesSprite(0x184, detailColor);
    }

    @Override
    public PortraitSprite makeRaisedRight(MyColors detailColor) {
        return new FaceAndClothesSprite(0x194, detailColor);
    }
}
