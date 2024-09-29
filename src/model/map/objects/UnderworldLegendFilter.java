package model.map.objects;

import model.Model;
import util.MyPair;
import view.BorderFrame;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UnderworldLegendFilter extends MapFilter {

    private static final int WIDTH = 13;
    private static final int HEIGHT = 5;
    private static final MyColors BACKGROUND_COLOR = MyColors.BEIGE;

    @Override
    public List<MyPair<Point, Sprite>> getObjects(Model model) {
        return new ArrayList<>();
    }

    @Override
    public void drawLegend(ScreenHandler screenHandler, int x, int y) {
        screenHandler.clearForeground(x, x+WIDTH, y, y+HEIGHT);
        BorderFrame.drawFrame(screenHandler, x, y, WIDTH, HEIGHT,
                MyColors.BLACK, MyColors.GRAY, BACKGROUND_COLOR, true);

        BorderFrame.drawString(screenHandler, "   CAVES    ", x+1, y+2, MyColors.BLACK, BACKGROUND_COLOR);
    }
}
