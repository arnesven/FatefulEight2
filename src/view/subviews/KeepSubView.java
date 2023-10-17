package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.races.Race;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class KeepSubView extends DailyActionSubView {

    public static final MyColors FLOOR_COLOR = MyColors.LIGHT_GRAY;

    private static final Sprite WALL = new Sprite32x32("keepfarwall", "world_foreground.png", 0x57,
            MyColors.DARK_GRAY, MyColors.BEIGE, MyColors.LIGHT_GRAY);
    private static final Sprite WALL2 = new Sprite32x32("keepfarwall2", "world_foreground.png", 0x58,
            MyColors.DARK_GRAY, MyColors.BEIGE, MyColors.LIGHT_GRAY);
    public static final Sprite FLOOR = new Sprite32x32("townhallfloor", "world_foreground.png", 0x56,
            MyColors.GRAY, FLOOR_COLOR, MyColors.TAN);
    public static final Sprite RUG = new Sprite32x32("townhallrug", "world_foreground.png", 0x72,
            MyColors.DARK_RED, FLOOR_COLOR, MyColors.TAN);
    public static final Sprite PLANT = new Sprite32x32("plant", "world_foreground.png", 0x45,
            MyColors.DARK_GRAY, MyColors.BLACK, MyColors.DARK_GREEN, MyColors.CYAN);
    private static final Sprite LORD = new Sprite32x32("lord", "world_foreground.png", 0x67,
            MyColors.BLACK, MyColors.DARK_BLUE, Race.NORTHERN_HUMAN.getColor(), MyColors.PURPLE);
    public static final Sprite THRONE = new Sprite32x32("throne", "world_foreground.png", 0x66,
            MyColors.DARK_BROWN, MyColors.GOLD, MyColors.RED, MyColors.CYAN);
    public static final Sprite COLUMN = new Sprite32x32("window", "world_foreground.png", 0x59,
            MyColors.BLACK, MyColors.WHITE, MyColors.BEIGE, MyColors.CYAN);
    private final boolean drawLord;

    public KeepSubView(AdvancedDailyActionState state, SteppingMatrix<DailyActionNode> matrix, boolean drawLord) {
        super(state, matrix);
        this.drawLord = drawLord;
    }

    @Override
    protected void drawBackground(Model model) {
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 8; ++col) {
                Point p = convertToScreen(new Point(col, row));
                if (row == 0) {
                    model.getScreenHandler().put(p.x, p.y-2, WALL);
                    model.getScreenHandler().put(p.x, p.y, WALL2);
                } else if (1 < col && col < 6) {
                    model.getScreenHandler().put(p.x, p.y, RUG);
                } else {
                    model.getScreenHandler().put(p.x, p.y, FLOOR);
                }
            }
        }
        drawDecorations(model);
        drawPartyArea(model, List.of(new Point(2, 5), new Point(4, 5),
                new Point(2, 6), new Point(4, 6), new Point(5, 5),
                new Point(5, 6), new Point(2, 4)));
    }

    private void drawDecorations(Model model) {
        for (int i = 2; i < 9; i+=2) {
            drawForeground(model, 1, i, COLUMN);
            drawForeground(model, 6, i, COLUMN);
        }
        drawForeground(model, 4, 1, THRONE);
        if (drawLord) {
            drawForeground(model, 4, 2, LORD);
        }
        drawForeground(model, 0, 1, PLANT);
        drawForeground(model, 0, 8, PLANT);
        drawForeground(model, 7, 1, PLANT);
        drawForeground(model, 7, 8, PLANT);
    }

    @Override
    protected String getPlaceType() {
        return "KEEP";
    }
}
