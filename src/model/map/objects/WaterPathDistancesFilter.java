package model.map.objects;

import model.Model;
import model.map.Direction;
import model.map.WaterPath;
import model.map.WorldHex;
import util.MyPair;
import view.BorderFrame;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.CharSprite;
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

    @Override
    public void drawSpecial(Model model, WorldHex[][] hexes, int x, int y, int screenX, int screenY) {
        for (WaterPath p : hexes[x][y].getWaterPaths()) {
            if (p.getHex() == hexes[x][y]) {
                String str = String.format("%X", p.getDistance());
                char dist = p.isDistanceUnset() ? 'X' : (str).charAt(0);
                if (p.getDistance() > 15) {
                    dist = '*';
                }
                int finalX = screenX;
                int finalY = screenY;

                switch (p.getDirection()) {
                    case Direction.NORTH:
                        finalX += 1;
                        break;
                    case Direction.NORTH_EAST:
                        finalX += 3;
                        finalY += 1;
                        break;
                    case Direction.SOUTH_EAST:
                        finalX += 3;
                        finalY += 3;
                        break;
                    case Direction.SOUTH:
                        finalX += 1;
                        finalY += 3;
                        break;
                    case Direction.SOUTH_WEST:
                        finalY += 3;
                    default:
                }

                model.getScreenHandler().register("sdas", new Point(finalX, finalY),
                        CharSprite.make(dist, MyColors.LIGHT_RED));
            }
        }
    }
}
