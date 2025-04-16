package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.TownDailyActionState;
import view.MyColors;
import view.combat.GrassCombatTheme;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.Random;

public class TownishSubView extends DailyActionSubView {

    public static final MyColors GROUND_COLOR = MyColors.GREEN;
    public static final MyColors GROUND_COLOR_NIGHT = MyColors.DARK_GREEN;
    public static final MyColors PATH_COLOR = MyColors.DARK_GRAY;
    public static final MyColors STREET_COLOR = MyColors.GRAY;
    public static final Sprite STREET = new Sprite32x32("streetground", "world_foreground.png", 0x02, GROUND_COLOR, PATH_COLOR, MyColors.TAN);
    private static final Sprite STREET_INNER = new Sprite32x32("streetground", "world_foreground.png", 0x02, STREET_COLOR, PATH_COLOR, MyColors.TAN);
    private static final Sprite WATER_DAY = new DockSprite(0xA6, MyColors.LIGHT_BLUE, MyColors.CYAN);
    private static final Sprite WATER_EVENING = new DockSprite(0xA6, MyColors.DARK_BLUE, MyColors.BLUE);
    private static final Sprite DOCK_DAY = new DockSprite(0xB6, MyColors.LIGHT_BLUE, MyColors.CYAN);
    private static final Sprite DOCK_EVENING = new DockSprite(0xB6, MyColors.DARK_BLUE, MyColors.BLUE);
    private static final Sprite STREET_DAY = new Sprite32x32("streetground", "world_foreground.png", 0x02,
            GROUND_COLOR, PATH_COLOR, MyColors.TAN);
    private static final Sprite STREET_EVENING = new Sprite32x32("streetground", "world_foreground.png", 0x02,
            GROUND_COLOR_NIGHT, PATH_COLOR, MyColors.TAN);

    private final boolean isCoastal;
    private final String townName;
    private final double townDensity;
    private final Sprite[] townHouses;
    private final AdvancedDailyActionState state;

    public TownishSubView(AdvancedDailyActionState state, SteppingMatrix<DailyActionNode> matrix,
                       boolean isCoastal, String townName, double townDensity, Sprite[] townHouseSprites) {
        super(state, matrix);
        this.isCoastal = isCoastal;
        this.townName = townName;
        this.townDensity = townDensity;
        this.townHouses = townHouseSprites;
        this.state = state;
    }

    @Override
    protected void drawBackground(Model model) {
        drawStreet(model);
        if (isCoastal) {
            drawDocks(model);
        } else {
            drawTopRowGrass(model);
        }
        Random rnd = new Random(townName.hashCode());
        for (int col = 0; col < 8; col++) {
            for (int row = 1; row < 8; row++) {
                if (getMatrix().getElementAt(col, row) == null
                        && rnd.nextDouble() > (1.0- townDensity)
                        && isOutsideTownSquare(col, row)
                        && !state.isPositionBlocked(col, row)) {
                    Point p = convertToScreen(new Point(col, row));
                    Sprite townHouse = townHouses[rnd.nextInt(townHouses.length)];
                    model.getScreenHandler().register(townHouse.getName(), p, townHouse);
                }
            }
        }
    }

    private boolean isOutsideTownSquare(int col, int row) {
        return !((1 < col && col < 5) && (2 < row && row < 6));
    }

    @Override
    protected String getUnderText(Model model) {
        return townName + ": " + super.getUnderText(model);
    }

    @Override
    protected String getPlaceType() {
        return "TOWN";
    }

    private void drawDocks(Model model) {
        Sprite waterSprite;
        Sprite dockSprite;
        if (model.getTimeOfDay() == TimeOfDay.EVENING) {
            waterSprite = WATER_EVENING;
            dockSprite = DOCK_EVENING;
        } else {
            waterSprite = WATER_DAY;
            dockSprite = DOCK_DAY;
        }
        for (int col = 0; col < TownDailyActionState.TOWN_MATRIX_COLUMNS; ++col) {
            Point p = convertToScreen(new Point(col, 0));
            model.getScreenHandler().put(p.x, p.y, dockSprite);
            p.y -= 2;
            model.getScreenHandler().put(p.x, p.y, waterSprite);
        }
    }

    private void drawTopRowGrass(Model model) {
        Random random = new Random(8);
        for (int col = 0; col < TownDailyActionState.TOWN_MATRIX_COLUMNS; ++col) {
            Point p = convertToScreen(new Point(col, 0));
            Sprite spr1;
            Sprite spr2;
            if (model.getTimeOfDay() == TimeOfDay.EVENING) {
                spr1 = GrassCombatTheme.darkGrassSprites[random.nextInt(GrassCombatTheme.darkGrassSprites.length)];
                spr2 = GrassCombatTheme.darkGrassSprites[random.nextInt(GrassCombatTheme.darkGrassSprites.length)];
            } else {
                spr1 = GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)];
                spr2 = GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)];
            }
            model.getScreenHandler().put(p.x, p.y, spr1);
            p.y -= 2;
            model.getScreenHandler().put(p.x, p.y, spr2);
        }
    }

    private void drawStreet(Model model) {
        Sprite street = STREET_DAY;
        if (model.getTimeOfDay() == TimeOfDay.EVENING) {
            street = STREET_EVENING;
        }

        Random random = new Random(1234);
        for (int row = 1; row < TownDailyActionState.TOWN_MATRIX_ROWS; ++row) {
            for (int col = 0; col < TownDailyActionState.TOWN_MATRIX_COLUMNS; ++col) {
                Point p = convertToScreen(new Point(col, row));
                if (col == TownDailyActionState.TOWN_MATRIX_COLUMNS-1
                        || row == TownDailyActionState.TOWN_MATRIX_ROWS-1) {
                    Sprite spr;
                    if (model.getTimeOfDay() == TimeOfDay.EVENING) {
                        spr = GrassCombatTheme.darkGrassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)];
                    } else {
                        spr = GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)];
                    }
                    model.getScreenHandler().put(p.x, p.y, spr);
                } else if (1 <= col && col <= 5 && 2 <= row && row <= 6) {
                    model.getScreenHandler().put(p.x, p.y, STREET_INNER);
                } else {
                    model.getScreenHandler().put(p.x, p.y, street);
                }
            }
        }
    }

    private static class DockSprite extends LoopingSprite {
        public DockSprite(int num, MyColors color1, MyColors color2) {
            super("docksprite"+num, "world_foreground.png", num, 32, 32);
            setFrames(4);
            setDelay(32);
            setColor1(color1);
            setColor2(color2);
            setColor3(MyColors.BROWN);
            setColor4(MyColors.DARK_GRAY);
        }
    }

}
