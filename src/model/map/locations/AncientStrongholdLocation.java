package model.map.locations;

import model.map.HexLocation;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.HexLocationSprite;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.awt.*;

public class AncientStrongholdLocation extends HexLocation {
    private static final Sprite TOWER_TOP = new AncientStrongholdUpperSprite();
    private final int direction;

    public AncientStrongholdLocation(int expandDirection) {
        super("Ancient Stronghold");
        this.direction = expandDirection;
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("astrongholdupper", 0x137, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.DARK_RED, MyColors.YELLOW);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("astrongholdupper", 0x127, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.DARK_RED, MyColors.YELLOW);
    }

    @Override
    public void drawUpperHalf(ScreenHandler screenHandler, int x, int y, int flag) {
        super.drawUpperHalf(screenHandler, x, y, flag);
        screenHandler.register(TOWER_TOP.getName(), new Point(x, y-4), TOWER_TOP);
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    private static class AncientStrongholdUpperSprite extends LoopingSprite {
        public AncientStrongholdUpperSprite() {
            super("ancientupper", "world_foreground.png", 0x98, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_GRAY);
            setColor3(MyColors.DARK_RED);
            setColor4(MyColors.YELLOW);
            setFrames(4);
        }
    }
}