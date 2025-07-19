package model.map.objects;

import model.Model;
import util.MyPair;
import view.BorderFrame;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;

import java.awt.*;
import java.util.List;

public class WaterPathDistancesFilter extends MapFilter {

    private static final int WIDTH = 13;
    private static final int HEIGHT = 5;
    private static final MyColors BACKGROUND_COLOR = MyColors.CYAN;

    @Override
    public List<MyPair<Point, Sprite>> getObjects(Model model) {
        return List.of();
    }

    @Override
    public void drawLegend(ScreenHandler screenHandler, int x, int y) {
        screenHandler.clearForeground(x, x+WIDTH, y, y+HEIGHT);
        BorderFrame.drawFrame(screenHandler, x, y, WIDTH, HEIGHT,
                MyColors.BLACK, MyColors.GRAY, BACKGROUND_COLOR, true);

        BorderFrame.drawString(screenHandler, "WATER PATHS ", x+1, y+2, MyColors.BLACK, BACKGROUND_COLOR);
        BorderFrame.drawString(screenHandler, "  (DEBUG)   ", x+1, y+3, MyColors.BLACK, BACKGROUND_COLOR);
    }
}
