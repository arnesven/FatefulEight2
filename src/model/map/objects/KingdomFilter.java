package model.map.objects;

import model.Model;
import model.map.CastleLocation;
import model.map.DiscoveredRoute;
import util.MyLists;
import util.MyPair;
import view.BorderFrame;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KingdomFilter extends MapFilter {

    private static final MyColors ARKVALE_COLOR = MyColors.CYAN;
    private static final MyColors BOGDOWN_COLOR = MyColors.RED;
    private static final MyColors SUNBLAZE_COLOR = MyColors.WHITE;
    private static final MyColors ARDH_COLOR = MyColors.PURPLE;

    private static final Map<MyColors, Sprite> OVERLAY_SPRITES = makeOverlaySprites();
    private static final int WIDTH = 13;
    private static final int HEIGHT = 6;
    private static final MyColors BACKGROUND_COLOR = MyColors.BLACK;

    @Override
    public List<MyPair<Point, Sprite>> getObjects(Model model) {
        Map<CastleLocation, List<Point>> kingdoms = model.getWorld().getKingdoms();
        List<MyPair<Point, Sprite>> result = new ArrayList<>();
        for (CastleLocation castle : kingdoms.keySet()) {
            result.addAll(MyLists.transform(kingdoms.get(castle),
                    p -> new MyPair<>(p, OVERLAY_SPRITES.get(castle.getCastleColor()))));
        }
        return result;
    }

    @Override
    public void drawLegend(ScreenHandler screenHandler, int x, int y) {
        screenHandler.clearForeground(x, x+WIDTH, y-1, y+HEIGHT-1);
        BorderFrame.drawFrame(screenHandler, x, y-1, WIDTH, HEIGHT,
                MyColors.BLACK, MyColors.GRAY, BACKGROUND_COLOR, true);

        BorderFrame.drawString(screenHandler, "  KINGDOMS  ", x+1, y, MyColors.WHITE, BACKGROUND_COLOR);
        BorderFrame.drawString(screenHandler, (char)(0xFF) + " Bogdown", x+1, y+1, BOGDOWN_COLOR, BACKGROUND_COLOR);
        BorderFrame.drawString(screenHandler, (char)(0xFF) + " Arkvale", x+1, y+2, ARKVALE_COLOR, BACKGROUND_COLOR);
        BorderFrame.drawString(screenHandler, (char)(0xFF) + " Sunblaze", x+1, y+3, SUNBLAZE_COLOR, BACKGROUND_COLOR);
        BorderFrame.drawString(screenHandler, (char)(0xFF) + " Ardh", x+1, y+4, ARDH_COLOR, BACKGROUND_COLOR);
    }

    private static Map<MyColors, Sprite> makeOverlaySprites() {
        return Map.of(
                MyColors.DARK_GREEN,
                new Sprite32x32("greenkingdom", "world.png", 0x67,
                        BOGDOWN_COLOR, MyColors.GRAY, MyColors.BEIGE),
                MyColors.WHITE,
                new Sprite32x32("whitekingdom", "world.png", 0x67,
                        ARKVALE_COLOR, MyColors.GRAY, MyColors.BEIGE),
                MyColors.YELLOW,
                new Sprite32x32("yellowkingdom", "world.png", 0x67,
                        SUNBLAZE_COLOR, MyColors.GRAY, MyColors.BEIGE),
                MyColors.BLUE,
                new Sprite32x32("bluekingdom", "world.png", 0x67,
                        ARDH_COLOR, MyColors.GRAY, MyColors.BEIGE));
    }
}
