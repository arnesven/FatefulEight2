package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.map.WaterLocation;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import sprites.CanalSprite;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class EasternPalaceSubView extends TownishSubView {
    private static final double DENSITY = 0.9;


    private static final Sprite ZEN_GARDEN = new Sprite32x32("zengarden", "world_foreground.png", 0x0F,
            PATH_COLOR, MyColors.BROWN, MyColors.DARK_GREEN, MyColors.LIGHT_BLUE);
    private static final Sprite BRIDGE_PARK =  new Sprite32x32("zengarden2", "world_foreground.png", 0x1F,
            PATH_COLOR, MyColors.BROWN, MyColors.DARK_GREEN, MyColors.LIGHT_BLUE);
    private static final Sprite[] HOUSE_SPRITES = new Sprite[]{
            new Sprite32x32("townhouse", "world_foreground.png", 0x43,
                    MyColors.YELLOW, PATH_COLOR, MyColors.RED, MyColors.LIGHT_GRAY),
            new Sprite32x32("townhouse3", "world_foreground.png", 0x73,
                    MyColors.YELLOW, PATH_COLOR, MyColors.RED, MyColors.WHITE),
            new Sprite32x32("townhouse", "world_foreground.png", 0x2F,
                    MyColors.YELLOW, PATH_COLOR, MyColors.RED, MyColors.WHITE),
            new Sprite32x32("townhouse", "world_foreground.png", 0x2F,
                    MyColors.YELLOW, PATH_COLOR, MyColors.RED, MyColors.LIGHT_GRAY),
            ZEN_GARDEN,
            BRIDGE_PARK
    };

    private static final Sprite CANAL_DAY = new CanalSprite(TimeOfDay.MIDDAY, false);
    private static final Sprite CANAL_NIGHT = new CanalSprite(TimeOfDay.EVENING, false);
    private static final Sprite CANAL_BEND_DAY = new Sprite32x32("canalbendday", "world_foreground.png", 0x3F,
            TownishSubView.GROUND_COLOR, MyColors.LIGHT_GREEN, MyColors.BROWN, MyColors.LIGHT_BLUE);
    private static final Sprite CANAL_BEND_NIGHT = new Sprite32x32("canalbendnight", "world_foreground.png", 0x3F,
            TownishSubView.GROUND_COLOR_NIGHT, MyColors.GREEN, MyColors.DARK_BROWN, MyColors.DARK_BLUE);

    public EasternPalaceSubView(AdvancedDailyActionState advancedDailyActionState,
                                SteppingMatrix<DailyActionNode> matrix, String name) {
        super(advancedDailyActionState, matrix, WaterLocation.riverside, name, DENSITY, false, HOUSE_SPRITES);
    }

    @Override
    protected void drawBackground(Model model) {
        super.drawBackground(model);
        // Use other canal sprite for evening
        for (int x = 4; x < 8; ++x) {
            Sprite toUse = model.getTimeOfDay() == TimeOfDay.EVENING ? CANAL_NIGHT : CANAL_DAY;
            Point p = convertToScreen(new Point(x, 8));
            model.getScreenHandler().register(toUse.getName(), p, toUse);
        }
        Sprite toUse = model.getTimeOfDay() == TimeOfDay.EVENING ? CANAL_BEND_NIGHT : CANAL_BEND_DAY;
        Point p = convertToScreen(new Point(3, 8));
        model.getScreenHandler().register(toUse.getName(), p, toUse);
    }

}
