package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.map.WaterLocation;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;

public class MiningTownSubView extends TownishSubView {

    private static final Sprite GROUND_DAY = new Sprite32x32("groundday", "world_foreground.png", 0x02,
            MyColors.TAN, MyColors.DARK_GRAY, MyColors.TAN);
    private static final Sprite GROUND_NIGHT = new Sprite32x32("groundnight", "world_foreground.png", 0x02,
            MyColors.DARK_GRAY, MyColors.BLACK, MyColors.TAN);
    private static final Sprite GROUND_TOP = new Sprite32x32("groundtop", "world_foreground.png", 0x72,
            MyColors.TAN, MyColors.DARK_GRAY, MyColors.TAN);
    private static final Sprite GROUND_TOP_NIGHT = new Sprite32x32("groundtopnight", "world_foreground.png", 0x72,
            MyColors.DARK_GRAY, MyColors.DARK_GRAY, MyColors.TAN);
    private static final Sprite HILLS_DAY = new Sprite32x32("hillsday", "past_foreground.png", 0x17,
            MyColors.BLACK, MyColors.TAN, MyColors.TAN, MyColors.CYAN);
    private static final Sprite HILLS_NIGHT = new Sprite32x32("hillsday", "past_foreground.png", 0x17,
            MyColors.BLACK, MyColors.DARK_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);

    public MiningTownSubView(String townName, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        super(advancedDailyActionState, matrix, WaterLocation.inland, townName, 0.2,
                false, AncientCitySubView.HOUSE_SPRITES, List.of(new Point(0, 0), new Point(1, 1)));
    }

    @Override
    protected void drawTopRowGrass(Model model) {
        for (int i = 0; i < 8; ++i) {
            boolean night = model.getTimeOfDay() == TimeOfDay.EVENING || model.getTimeOfDay() == TimeOfDay.NIGHT;
            Point p = convertToScreen(new Point(i, 0));
            model.getScreenHandler().put(p.x, p.y - 2, night ? HILLS_NIGHT : HILLS_DAY);
        }
    }

    @Override
    protected void drawStreet(Model model) {
        super.drawStreet(model);
        for (int y = 0; y < 8; ++y) {
            for (int i = 0; i < 8; ++i) {
                Point p = convertToScreen(new Point(i, y));
                boolean night = model.getTimeOfDay() == TimeOfDay.EVENING || model.getTimeOfDay() == TimeOfDay.NIGHT;
                if (y <= 1) {
                    model.getScreenHandler().put(p.x, p.y, night ? GROUND_TOP_NIGHT : GROUND_TOP);
                } else {
                    model.getScreenHandler().put(p.x, p.y, night ? GROUND_NIGHT : GROUND_DAY);
                }
            }
        }
    }
}
