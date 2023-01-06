package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.TownDailyActionState;
import sprites.CombatCursorSprite;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x16;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class TownSubView extends DailyActionSubView {
    public static final MyColors GROUND_COLOR = MyColors.GREEN;
    public static final MyColors PATH_COLOR = MyColors.DARK_GRAY;
    public static final MyColors STREET_COLOR = MyColors.GRAY;
    private static final Sprite STREET = new Sprite32x32("streetground", "world_foreground.png", 0x02, GROUND_COLOR, PATH_COLOR, MyColors.TAN);
    private static final Sprite STREET_INNER = new Sprite32x32("streetground", "world_foreground.png", 0x02, STREET_COLOR, PATH_COLOR, MyColors.TAN);
    private static final Sprite WATER = makeWaterSprite();
    private static final Sprite DOCK = new Sprite32x32("towndock", "world_foreground.png", 0x42,
            MyColors.LIGHT_BLUE, MyColors.DARK_BLUE, MyColors.BROWN, MyColors.DARK_GRAY);
    private final boolean isCoastal;

    public TownSubView(TownDailyActionState state, SteppingMatrix<DailyActionNode> matrix, boolean isCoastal) {
        super(state, matrix);
        this.isCoastal = isCoastal;
    }

    @Override
    protected void drawBackground(Model model) {
        drawStreet(model);
        if (isCoastal) {
            drawDocks(model);
        } else {
            // TODO: draw more grass
        }
    }

    @Override
    protected String getPlaceType() {
        return "TOWN";
    }

    private void drawDocks(Model model) {
        for (int col = 0; col < TownDailyActionState.TOWN_MATRIX_COLUMNS; ++col) {
            Point p = convertToScreen(new Point(col, 0));
            model.getScreenHandler().put(p.x, p.y, DOCK);
            p.y -= 2;
            model.getScreenHandler().put(p.x, p.y, WATER);
        }
    }

    private void drawStreet(Model model) {
        Random random = new Random(1234);
        for (int row = 1; row < TownDailyActionState.TOWN_MATRIX_ROWS; ++row) {
            for (int col = 0; col < TownDailyActionState.TOWN_MATRIX_COLUMNS; ++col) {
                Point p = convertToScreen(new Point(col, row));
                if (col == TownDailyActionState.TOWN_MATRIX_COLUMNS-1
                        || row == TownDailyActionState.TOWN_MATRIX_ROWS-1) {
                    model.getScreenHandler().put(p.x, p.y,
                            GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)]);
                } else if (1 <= col && col <= 5 && 2 <= row && row <= 6) {
                    model.getScreenHandler().put(p.x, p.y, STREET_INNER);
                } else {
                    model.getScreenHandler().put(p.x, p.y, STREET);
                }
            }
        }
    }

    private static Sprite makeWaterSprite() {
        Sprite sprite = new Sprite32x16("water", "world.png", 0x40);
        sprite.setColor1(MyColors.LIGHT_BLUE);
        sprite.setColor2(MyColors.BLUE);
        return sprite;
    }

}
