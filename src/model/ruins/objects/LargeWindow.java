package model.ruins.objects;

import model.Model;
import model.ruins.themes.DungeonTheme;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class LargeWindow extends PositionableRoomDecoration {
    private static final Sprite SPRITE1 = new Sprite32x32("dungeonwindow", "dungeon.png", 0x57,
            MyColors.BLACK, MyColors.BEIGE, MyColors.DARK_GRAY, MyColors.LIGHT_BLUE);
    private static final Sprite SPRITE2 = new Sprite32x32("dungeonwindow", "dungeon.png", 0x67,
            MyColors.BLACK, MyColors.BEIGE, MyColors.DARK_GRAY, MyColors.LIGHT_BLUE);
    private static final Sprite SPRITE3 = new Sprite32x32("dungeonwindow", "dungeon.png", 0x66,
            MyColors.BLACK, MyColors.GREEN, MyColors.DARK_GRAY, MyColors.LIGHT_BLUE);
    private final Sprite sprite;

    public LargeWindow(int x, int y, boolean onGround) {
        super(x, y);
        if (onGround) {
            this.sprite = SPRITE3;
        } else {
            this.sprite = MyRandom.flipCoin() ? SPRITE1 : SPRITE2;
        }
    }

    public LargeWindow(int x, int y) {
        this(x, y, false);
    }

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return sprite;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos, DungeonTheme theme) {
        model.getScreenHandler().register(getSprite(theme).getName(), new Point(xPos, yPos), getSprite(theme));
    }

    @Override
    public String getDescription() {
        return "A window";
    }
}
