package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.ExitTavernNode;
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
    private final boolean inTown;

    public TavernSubView(AdvancedDailyActionState state, SteppingMatrix<DailyActionNode> matrix, boolean inTown) {
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
            Point p = convertToScreen(new Point(3, 7));
            model.getScreenHandler().put(p.x, p.y, ExitTavernNode.DOOR);
        }
    }

    @Override
    protected String getPlaceType() {
        if (inTown) {
            return "TAVERN";
        }
        return "INN";
    }
}
