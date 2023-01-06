package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.races.Race;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.ExitTavernNode;
import model.states.dailyaction.TavernDailyActionState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.Random;

public class TavernSubView extends DailyActionSubView {
    public static final MyColors FLOOR_COLOR = MyColors.DARK_BROWN;

    private static final Sprite WALL = new Sprite32x32("tavernfarwall", "world_foreground.png", 0x44,
            MyColors.DARK_GRAY, MyColors.BROWN, MyColors.TAN);
    private static final Sprite SIDE_WALL = new Sprite32x32("sidewall", "world_foreground.png", 0x14,
            MyColors.DARK_GRAY, MyColors.BROWN, MyColors.BROWN);
    public static final Sprite FLOOR = new Sprite32x32("tavernfloor", "combat.png", 0x53,
            MyColors.BROWN, FLOOR_COLOR, MyColors.TAN);
    private static final Sprite LOWER_WALL = new Sprite32x32("lowerwall", "world_foreground.png", 0x24,
            MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW, MyColors.TAN);
    private static final Sprite BAR_UPPER = new Sprite32x32("barupper", "world_foreground.png", 0x15,
            MyColors.BLACK, MyColors.TAN, Race.NORTHERN_HUMAN.getColor(), MyColors.BEIGE);
    private static final Sprite BAR_LOWER = new Sprite32x32("barlower", "world_foreground.png", 0x25,
            MyColors.BLACK, MyColors.TAN, MyColors.BROWN);
    private static final Sprite WINDOW = new Sprite32x32("window", "world_foreground.png", 0x35,
            MyColors.BLACK, MyColors.BLACK, MyColors.GREEN, MyColors.CYAN);
    private static final Sprite PLANT = new Sprite32x32("plant", "world_foreground.png", 0x45,
            MyColors.DARK_GRAY, MyColors.BLACK, MyColors.DARK_GREEN, MyColors.CYAN);
    private final boolean inTown;

    public TavernSubView(AdvancedDailyActionState state,
                         SteppingMatrix<DailyActionNode> matrix, boolean inTown) {
        super(state, matrix);
        this.inTown = inTown;
    }

    @Override
    protected void drawBackground(Model model) {
        Random random = new Random(9847);
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 8; ++col) {
                Point p = convertToScreen(new Point(col, row));
                if ((col == 0 || col == 7) && row < 7) {
                    model.getScreenHandler().put(p.x, p.y, SIDE_WALL);
                } else if (0 < row && row < 7) {
                    model.getScreenHandler().put(p.x, p.y, FLOOR);
                } else if (row == 0) {
                    if (col == 6) {
                        model.getScreenHandler().put(p.x, p.y, SIDE_WALL);
                    } else {
                        model.getScreenHandler().put(p.x, p.y, WALL);
                    }
                } else if (row == 7) {
                    model.getScreenHandler().put(p.x, p.y, LOWER_WALL);
                } else {
                    model.getScreenHandler().put(p.x, p.y,
                            GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)]);
                }
            }
        }
        if (!inTown) {
            Point p = convertToScreen(TavernDailyActionState.getDoorPosition());
            model.getScreenHandler().put(p.x, p.y, ExitTavernNode.DOOR);
        }

        drawForeground(model, 2, 0, WINDOW);
        drawForeground(model, 1, 1, PLANT);
        drawForeground(model, 1, 3, BAR_UPPER);
        drawForeground(model, 1, 4, BAR_LOWER);

    }

    private void drawForeground(Model model, int x, int y, Sprite sprite) {
        Point p = convertToScreen(new Point(x, y));
        model.getScreenHandler().register(sprite.getName(), p, sprite);
    }

    @Override
    protected String getPlaceType() {
        if (inTown) {
            return "TAVERN";
        }
        return "INN";
    }

    public void animateMovement(Model model, Point from, Point to) {
        if (insideToOutside(from, to) || insideToOutside(to, from)) {
            super.animateMovement(model, from, TavernDailyActionState.getDoorPosition());
            super.animateMovement(model, TavernDailyActionState.getDoorPosition(), to);
        } else {
            super.animateMovement(model, from, to);
        }
    }

    private boolean insideToOutside(Point from, Point to) {
        return to.y != AdvancedDailyActionState.TOWN_MATRIX_ROWS-1 &&
                from.y == AdvancedDailyActionState.TOWN_MATRIX_ROWS-1;
    }

}
