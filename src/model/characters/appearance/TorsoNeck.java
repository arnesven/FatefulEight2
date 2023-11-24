package model.characters.appearance;

import view.MyColors;
import view.sprites.PortraitSprite;

import java.io.Serializable;

public interface TorsoNeck extends Serializable {
    void makeNaked(PortraitSprite[][] grid);
    PortraitSprite makeCenter(MyColors skinColor);

    PortraitSprite makeLeft(MyColors skinColor);
    PortraitSprite makeRight(MyColors skinColor);

    PortraitSprite getHoodLeft(MyColors color);
    PortraitSprite getHoodRight(MyColors color);

    PortraitSprite getArmorLeft(MyColors armorColor, MyColors underShirtColor);
    PortraitSprite getArmorRight(MyColors armorColor, MyColors underShirtColor);

    PortraitSprite makeFancyLeft(MyColors color, MyColors detailColor);
    PortraitSprite makeFancyRight(MyColors color, MyColors detailColor);

    PortraitSprite makeWideLeft(MyColors color);
    PortraitSprite makeWideRight(MyColors color);

    PortraitSprite makeRaisedLeft(MyColors detailColor);
    PortraitSprite makeRaisedRight(MyColors detailColor);
}
