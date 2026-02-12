package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.map.UrbanLocation;
import model.map.WaterLocation;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.TownDailyActionState;
import view.MyColors;
import view.combat.GrassCombatTheme;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.sprites.Sprite32x64;

import java.awt.*;
import java.util.Random;

public class AncientCitySubView extends TownishSubView {
    private static final Sprite[] HOUSE_SPRITES = new Sprite[]{
            new Sprite32x64("tallbldg", "world_foreground.png", 0x70,
                    MyColors.BLACK, MyColors.LIGHT_YELLOW, MyColors.GOLD, MyColors.DARK_BROWN),
            new Sprite32x64("tallbldg", "world_foreground.png", 0x71,
                    MyColors.BLACK, MyColors.PINK, MyColors.GRAY_RED, MyColors.BROWN),
    };

    private static final Sprite TOP_ROW_DAY = new Sprite32x64("tallbldgbg", "world_foreground.png", 0x73,
            MyColors.BLACK, MyColors.BEIGE, MyColors.DARK_GRAY, MyColors.GRAY);
    private static final Sprite TOP_ROW_TIP_DAY = new Sprite32x32("tallbldgbg", "world_foreground.png", 0xE4,
            MyColors.BLACK, MyColors.BEIGE, MyColors.DARK_GRAY, MyColors.CYAN);
    private static final Sprite TOP_ROW_NIGHT = new Sprite32x64("tallbldgbg", "world_foreground.png", 0x73,
            MyColors.BLACK, MyColors.BEIGE, MyColors.DARK_GRAY, MyColors.GRAY);
    private static final Sprite TOP_ROW_TIP_NIGHT = new Sprite32x32("tallbldgbg", "world_foreground.png", 0xE4,
            MyColors.BLACK, MyColors.BEIGE, MyColors.DARK_GRAY, MyColors.DARK_BLUE);

    public AncientCitySubView(AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix, String name) {
        super(advancedDailyActionState, matrix, WaterLocation.inland, name, 0.6, true, HOUSE_SPRITES);
    }

    @Override
    protected String getPlaceType() {
        return "ANCIENT CITY";
    }

    @Override
    protected void drawTopRowGrass(Model model) {
        // Not needed.
    }

    @Override
    protected void drawBackground(Model model) {
        super.drawBackground(model);
        for (int i = 0; i < 8; ++i) {
            Point p = convertToScreen(new Point(i, 0));
            if (model.getTimeOfDay() == TimeOfDay.EVENING ||model.getTimeOfDay() == TimeOfDay.NIGHT) {
                model.getScreenHandler().put(p.x, p.y-2, TOP_ROW_TIP_NIGHT);
                model.getScreenHandler().put(p.x, p.y, TOP_ROW_NIGHT);
            } else {
                model.getScreenHandler().put(p.x, p.y-2, TOP_ROW_TIP_DAY);
                model.getScreenHandler().put(p.x, p.y, TOP_ROW_DAY);
            }
        }
    }

    protected void drawStreet(Model model) {

        Random random = new Random(1234);
        for (int row = 2; row < TownDailyActionState.TOWN_MATRIX_ROWS; ++row) {
            for (int col = 0; col < TownDailyActionState.TOWN_MATRIX_COLUMNS; ++col) {
                Point p = convertToScreen(new Point(col, row));
                if (row == TownDailyActionState.TOWN_MATRIX_ROWS-1) {
                    Sprite spr;
                    if (model.getTimeOfDay() == TimeOfDay.EVENING) {
                        spr = GrassCombatTheme.darkGrassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)];
                    } else {
                        spr = GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)];
                    }
                    model.getScreenHandler().put(p.x, p.y, spr);
                } else {
                    model.getScreenHandler().put(p.x, p.y, STREET_INNER);
                }
            }
        }
    }
}
