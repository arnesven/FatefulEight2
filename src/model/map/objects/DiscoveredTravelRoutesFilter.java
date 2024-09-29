package model.map.objects;

import model.Model;
import model.map.DiscoveredRoute;
import util.MyPair;
import view.BorderFrame;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DiscoveredTravelRoutesFilter extends MapFilter {

    private static final int WIDTH = 13;
    private static final int HEIGHT = 5;
    private static final MyColors BACKGROUND_COLOR = MyColors.BEIGE;

    @Override
    public List<MyPair<Point, Sprite>> getObjects(Model model) {
        List<DiscoveredRoute> discoveredRoutes = model.getParty().getDiscoveredRoutes();
        List<MyPair<Point, Sprite>> result = new ArrayList<>();
        for (DiscoveredRoute discoveredRoute : discoveredRoutes) {
            result.addAll(discoveredRoute.getMapObjects());
        }
        return result;
    }

    @Override
    public void drawLegend(ScreenHandler screenHandler, int x, int y) {
        screenHandler.clearForeground(x, x+WIDTH, y, y+HEIGHT);
        BorderFrame.drawFrame(screenHandler, x, y, WIDTH, HEIGHT,
                MyColors.BLACK, MyColors.GRAY, BACKGROUND_COLOR, true);

        BorderFrame.drawString(screenHandler, "KNOWN ROUTES", x+1, y+1, MyColors.BLACK, BACKGROUND_COLOR);
        BorderFrame.drawString(screenHandler, (char)(0x7F) + " BOAT", x+1, y+2, DiscoveredRoute.SHIP_COLOR, BACKGROUND_COLOR);
        BorderFrame.drawString(screenHandler, (char)(0x7F) + " CARRIAGE", x+1, y+3, DiscoveredRoute.CARRIAGE_COLOR, BACKGROUND_COLOR);
        BorderFrame.drawString(screenHandler, (char)(0x7F) + " TELEPORT", x+1, y+4, DiscoveredRoute.TELEPORT_COLOR, BACKGROUND_COLOR);

    }

}
