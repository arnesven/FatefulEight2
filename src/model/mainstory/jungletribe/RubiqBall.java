package model.mainstory.jungletribe;

import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.Map;

public class RubiqBall {
    private static final Map<MyColors, Sprite> BALL_SPRITES = Map.of(
            MyColors.RED,
            new Sprite32x32("redball", "quest.png", 0xDD, MyColors.DARK_RED, MyColors.RED, MyColors.GRAY_RED, MyColors.BEIGE),
            MyColors.YELLOW,
            new Sprite32x32("yellowball", "quest.png", 0xDD, MyColors.GOLD, MyColors.YELLOW, MyColors.GRAY_RED, MyColors.BEIGE),
            MyColors.BLUE,
            new Sprite32x32("blueball", "quest.png", 0xDD, MyColors.DARK_BLUE, MyColors.BLUE, MyColors.GRAY_RED, MyColors.BEIGE),
            MyColors.GREEN,
            new Sprite32x32("greenball", "quest.png", 0xDD, MyColors.DARK_GREEN, MyColors.GREEN, MyColors.GRAY_RED, MyColors.BEIGE));
    private final Sprite sprite;
    private final MyColors color;

    public RubiqBall(MyColors color) {
        this.color = color;
        this.sprite = BALL_SPRITES.get(color);
    }

    public void drawYourself(ScreenHandler screenHandler, Point point) {
        screenHandler.register(sprite.getName(), point, sprite, 2);
    }

    public MyColors getColor() {
        return color;
    }
}
