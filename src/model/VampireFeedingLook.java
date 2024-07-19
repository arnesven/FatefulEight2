package model;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite8x8;

import java.awt.*;
import java.io.Serializable;

public class VampireFeedingLook implements Serializable {
    private final Point point;
    private final Sprite8x8 mouthSprite;
    private static final Sprite8x8 EYE_SPRITE = new Sprite8x8("vampirefeedingeyes", "mouth.png", 0x32,
            MyColors.BLACK, MyColors.GRAY, MyColors.YELLOW, MyColors.CYAN);
    private final Point pointLeft;
    private final Point pointRight;

    public VampireFeedingLook(CharacterAppearance app, Point p) {
        this.point = new Point(p.x, p.y+5);
        this.mouthSprite = new Sprite8x8("vampiremouth", "mouth.png", 0x4A,
                MyColors.BLACK, app.getLipColor(), MyColors.WHITE,
                app.hasAlternateSkinColor() ? app.getAlternateSkinColor() : app.getRace().getColor());
        this.pointLeft = new Point(point.x-1, point.y-1);
        this.pointRight = new Point(point.x+1, point.y-1);
    }

    public void drawYourself(ScreenHandler screenHandler) {
        screenHandler.register(mouthSprite.getName(), point, mouthSprite);
        screenHandler.register(EYE_SPRITE.getName(), pointLeft, EYE_SPRITE, 3);
        screenHandler.register(EYE_SPRITE.getName(), pointRight, EYE_SPRITE, 3);
    }
}
