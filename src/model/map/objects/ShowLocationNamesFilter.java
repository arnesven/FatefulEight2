package model.map.objects;

import model.Model;
import model.map.HexLocation;
import model.map.WorldHex;
import util.MyPair;
import view.BorderFrame;
import view.DrawingArea;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;

import java.awt.*;
import java.util.List;

public class ShowLocationNamesFilter extends MapFilter {

    private static final int WIDTH = 13;
    private static final int HEIGHT = 5;
    private static final MyColors BACKGROUND_COLOR = MyColors.CYAN;

    @Override
    public List<MyPair<Point, Sprite>> getObjects(Model model) {
        return List.of();
    }

    @Override
    public void drawLegend(ScreenHandler screenHandler, int x, int y) {}

    @Override
    public void drawSpecial(Model model, WorldHex[][] hexes, int x, int y, int screenX, int screenY) {
        HexLocation loc = hexes[x][y].getLocation();
        if (loc != null && !loc.isDecoration() && loc.showNameOnMap()) {
            String name = loc.getName().replace("Town of ", "");
            String[] parts = name.split(" ");
            for (int i = 0; i < parts.length; ++i) {

                int finalX = Math.min(DrawingArea.WINDOW_COLUMNS - parts[i].length(),
                            Math.max(0, screenX + 2 - parts[i].length() / 2));

                BorderFrame.drawStringInForeground(model.getScreenHandler(), parts[i],
                        finalX, 2 + screenY + i, MyColors.WHITE, MyColors.BLACK);
            }
        }
    }
}
