package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.races.Race;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import view.MyColors;
import view.combat.GrassCombatTheme;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class TownHallSubView extends DailyActionSubView {
    public static final MyColors FLOOR_COLOR = MyColors.LIGHT_GRAY;
    public static final Sprite DOOR = new Sprite32x32("door", "world_foreground.png", 0x34,
            MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW, MyColors.TAN, MyColors.DARK_RED);
    private static final Sprite WALL = new Sprite32x32("tavernfarwall", "world_foreground.png", 0x44,
            MyColors.DARK_GRAY, MyColors.BROWN, MyColors.TAN);
    public static final Sprite FLOOR = new Sprite32x32("townhallfloor", "world_foreground.png", 0x56,
            MyColors.GRAY, FLOOR_COLOR, MyColors.TAN);
    private static final Sprite LOWER_WALL = new Sprite32x32("lowerwall", "world_foreground.png", 0x24,
            MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW, MyColors.TAN);
    public static final Sprite RUG = new Sprite32x32("townhallrug", "world_foreground.png", 0x72,
            MyColors.DARK_RED, FLOOR_COLOR, MyColors.TAN);
    private static final Sprite PLANT = new Sprite32x32("plant", "world_foreground.png", 0x45,
            MyColors.DARK_GRAY, MyColors.BLACK, MyColors.DARK_GREEN, MyColors.CYAN);
    private static final Sprite LORD = new Sprite32x32("lord", "world_foreground.png", 0x67,
            MyColors.BLACK, MyColors.DARK_BLUE, Race.NORTHERN_HUMAN.getColor(), MyColors.PURPLE);
    private static final Sprite THRONE = new Sprite32x32("throne", "world_foreground.png", 0x66,
            MyColors.DARK_BROWN, MyColors.GOLD, MyColors.RED, MyColors.CYAN);
    private static final Sprite WINDOW = new Sprite32x32("window", "world_foreground.png", 0x35,
            MyColors.BLACK, MyColors.BLACK, MyColors.GREEN, MyColors.CYAN);
    private final boolean drawLord;

    public TownHallSubView(AdvancedDailyActionState state, SteppingMatrix<DailyActionNode> matrix, boolean drawLord) {
        super(state, matrix);
        this.drawLord = drawLord;
    }

    @Override
    protected void drawBackground(Model model) {
        Random random = new Random(9847);
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 8; ++col) {
                Point p = convertToScreen(new Point(col, row));
                if (0 < row && row < 7 && 1 < col && col < 6) {
                    model.getScreenHandler().put(p.x, p.y, RUG);
                } else if (0 < row && row < 7) {
                    model.getScreenHandler().put(p.x, p.y, FLOOR);
                } else if (row == 0) {
                    model.getScreenHandler().put(p.x, p.y, WALL);
                } else if (row == 7) {
                    model.getScreenHandler().put(p.x, p.y, LOWER_WALL);
                } else {
                    // TODO: If !drawLord, make dark grass sprites.
                    model.getScreenHandler().put(p.x, p.y,
                            GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)]);
                }
            }
        }
        drawDecorations(model);
        drawPartyArea(model, List.of(new Point(2, 5), new Point(4, 5),
                new Point(2, 6), new Point(4, 6), new Point(5, 5),
                new Point(5, 6), new Point(2, 4)));
    }

    private void drawDecorations(Model model) {
        drawForeground(model, 1, 0, WINDOW);
        drawForeground(model, 3, 0, WINDOW); // TODO: If !drawLord, make evening window.
        drawForeground(model, 4, 0, WINDOW);
        drawForeground(model, 6, 0, WINDOW);
        drawForeground(model, 0, 1, PLANT);
        drawForeground(model, 7, 1, PLANT);
        drawForeground(model, 0, 6, PLANT);
        drawForeground(model, 7, 6, PLANT);
        drawForeground(model, 4, 1, THRONE);
        if (drawLord) {
            drawForeground(model, 4, 2, LORD);
        }
    }

    @Override
    protected String getPlaceType() {
        return "TOWN HALL";
    }
}
