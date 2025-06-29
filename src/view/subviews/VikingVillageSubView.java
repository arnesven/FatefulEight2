package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.TownDailyActionState;
import view.MyColors;
import view.combat.GrassCombatTheme;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.Random;

public class VikingVillageSubView extends TownishSubView {

    private static final Sprite[] HOUSE_SPRITES = new Sprite[]{
            new Sprite32x32("townhouse", "world_foreground.png", 0x43,
                    MyColors.YELLOW, PATH_COLOR, MyColors.WHITE, MyColors.GOLD),
            new Sprite32x32("townhouse2", "world_foreground.png", 0x53,
                    MyColors.YELLOW, PATH_COLOR, MyColors.WHITE, MyColors.GOLD),
            new Sprite32x32("townhouse3", "world_foreground.png", 0x73,
                    MyColors.YELLOW, PATH_COLOR, MyColors.WHITE, MyColors.GOLD)};

    private static final double TOWN_DENSITY = 0.35;

    private static final Sprite STREET_DAY = new Sprite32x32("streetground", "world_foreground.png", 0x02,
            MyColors.WHITE, PATH_COLOR, MyColors.TAN);
    private static final Sprite STREET_EVENING = new Sprite32x32("streetground", "world_foreground.png", 0x02,
            MyColors.LIGHT_GRAY, PATH_COLOR, MyColors.TAN);
    private static final Sprite DARK_SNOW = new Sprite32x32("darksnow", "world_foreground.png", 0x72,
            MyColors.LIGHT_GRAY, MyColors.BEIGE, MyColors.BLACK);
    private static final Sprite SNOW = new Sprite32x32("lightsnow", "world_foreground.png", 0x72,
            MyColors.WHITE, MyColors.BEIGE, MyColors.BLACK);


    public VikingVillageSubView(AdvancedDailyActionState state, SteppingMatrix<DailyActionNode> matrix, boolean isCoastal, String name) {
        super(state, matrix, isCoastal, name, TOWN_DENSITY, true, HOUSE_SPRITES);
    }

    @Override
    protected String getPlaceType() {
        return "VIKING VILLAGE";
    }

    protected void drawStreet(Model model) {
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
                        spr = DARK_SNOW;
                    } else {
                        spr = SNOW;
                    }
                    model.getScreenHandler().put(p.x, p.y, spr);
                } else {
                    model.getScreenHandler().put(p.x, p.y, street);
                }
            }
        }
    }
}
