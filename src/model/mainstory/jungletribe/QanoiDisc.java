package model.mainstory.jungletribe;

import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite32x16;

import java.awt.*;

public class QanoiDisc {

    private static final Sprite32x16[] SPRITES = new Sprite32x16[]{
            new QanoiDiscSprite(0x1D2, MyColors.RED),
            new QanoiDiscSprite(0x1D3, MyColors.GREEN),
            new QanoiDiscSprite(0x1C2, MyColors.YELLOW),
            new QanoiDiscSprite(0x1C3, MyColors.BLUE)
    };

    private final int size;
    private final Sprite32x16 sprite;

    public QanoiDisc(int size) {
        this.size = size;
        this.sprite = SPRITES[size-1];
    }

    public void drawYourself(ScreenHandler screenHandler, Point newPos, int layer, int yshift) {
        screenHandler.register(sprite.getName(), newPos, sprite, layer, 0, 2 + 6 * yshift);
    }

    public int getSize() {
        return size;
    }

    private static class QanoiDiscSprite extends Sprite32x16 {

        public QanoiDiscSprite(int num, MyColors color) {
            super("qanoidisc" + num, "quest.png", num);
            setColor1(MyColors.BLACK);
            setColor2(color);
        }
    }
}
