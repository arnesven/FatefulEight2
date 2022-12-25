package view.sprites;

import model.Model;
import model.races.Race;
import view.MyColors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class AvatarSprite extends LoopingSprite {

    private Sprite32x32 deadSprite;
    private int currentFrame = 0;
    private int count = 0;
    private int delay = 16;

    public AvatarSprite(Race race, int num, MyColors color2, MyColors color4) {
        super("partymarker", "avatars.png", num+(race.isShort()?4:0), 32);
        setFrames(4);
        setColor1(MyColors.BLACK);
        setColor2(color2);
        setColor3(race.getColor());
        setColor4(color4);
        deadSprite = new Sprite32x32("deadavatar", "avatars.png", num+3, MyColors.BLACK, color2, race.getColor());
        deadSprite.setColor4(color4);
    }

    public AvatarSprite(Race race, int num, MyColors color2) {
        this(race, num, color2, color2);
    }

    public Sprite getDead() {
        return deadSprite;
    }

    @Override
    protected int getCurrentFrameIndex(int currentFrame) {
        switch (currentFrame) {
            case 2:
                return 0;
            case 3:
                return 2;
            default:
                return currentFrame;
        }
    }
}
