package model.mainstory.jungletribe;

import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x16;

import java.awt.*;
import java.util.ArrayList;

public class QanoiPin extends ArrayList<QanoiDisc> {
    private static final Sprite BASE = new PinSprite(0x1D4);
    private static final Sprite SHAFT = new PinSprite(0x1C4);
    private static final Sprite TIP = new PinSprite(0x1C5);

    public void drawYourself(ScreenHandler screenHandler, Point position) {
        screenHandler.register(BASE.getName(), position, BASE);
        for (int y = 1; y <= 4; ++y) {
            Point newPos = new Point(position.x, position.y - 2*y);
            screenHandler.register(SHAFT.getName(), newPos, SHAFT);
            if (y <= this.size()) {
                this.get(y-1).drawYourself(screenHandler, newPos);
            }
        }
        screenHandler.register(TIP.getName(), new Point(position.x, position.y - 10), SHAFT);
    }

    public QanoiDisc getTopDisc() {
        return remove(size()-1);
    }

    private static class PinSprite extends Sprite32x16 {
        public PinSprite(int num) {
            super("pinbase", "quest.png", num);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.GRAY_RED);
        }
    }
}
