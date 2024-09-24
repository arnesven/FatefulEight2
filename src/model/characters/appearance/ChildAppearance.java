package model.characters.appearance;

import model.races.ElvenRace;
import model.races.Race;
import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;

import java.awt.*;
import java.util.List;

public class ChildAppearance extends CharacterAppearance {
    private final Sprite headSprite;
    private final boolean realGender;

    public ChildAppearance(Race race, boolean gender, MyColors hairColor) {
        super(race, false, hairColor);
        this.realGender = gender;
        setShoulders(ShouldersFactory.makeShoulders("Narrow", false));
        setNeck(new SlenderNeck());

        int hairSpriteNum = 2 + MyRandom.randInt(3);
        if (gender) {
            hairSpriteNum += 3;
        }
        Sprite hairSprite = new Sprite("childhair", "child.png", hairSpriteNum, 0, 24, 40);
        hairSprite.setColor1(MyColors.BLACK);
        hairSprite.setColor2(hairColor);
        hairSprite.setColor3(MyColors.CYAN);

        headSprite = new Sprite("childhead", "child.png", race instanceof ElvenRace ? 1 : 0,
                0, 24, 40, List.of(hairSprite));
        headSprite.setColor1(race.getColor());
        headSprite.setColor2(MyColors.WHITE);
        headSprite.setColor3(race.getMouthDefaultColor());
        headSprite.setColor4(MyColors.BLACK);


    }

    @Override
    public boolean getGender() {
        return realGender;
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        super.drawYourself(screenHandler, col, row);
        screenHandler.clearSpace(col+1, col+6, row+1, row+5);
        screenHandler.register(headSprite.getName(), new Point(col+2, row+1), headSprite);
    }


    @Override
    public void drawBlink(ScreenHandler screenHandler, int x, int y) { }

    @Override
    public void drawDrawLook(ScreenHandler screenHandler, boolean left, int x, int y) { }
    
    @Override
    public boolean supportsSpeakingAnimation() {
        return false;
    }

    @Override
    protected int getMouth() {
        return 0;
    }

    @Override
    protected int getEye() {
        return 0;
    }

    @Override
    public int getNose() {
        return 0;
    }

    @Override
    public boolean hairInForehead() {
        return false;
    }

    @Override
    public boolean hairOnTop() {
        return false;
    }

    @Override
    public CharacterAppearance copy() {
        return new ChildAppearance(getRace(), getGender(), getHairColor());
    }
}
