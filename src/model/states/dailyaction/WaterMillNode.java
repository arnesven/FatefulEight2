package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.states.GameState;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownishSubView;

import java.awt.*;
import java.util.List;

public class WaterMillNode extends DailyActionNode {

    private static final Sprite BLDG_LOWER = new Sprite32x32("bldglower", "world_foreground.png",
            0x7C, MyColors.YELLOW, TownishSubView.PATH_COLOR, MyColors.RED, MyColors.LIGHT_GRAY);
    private static final Sprite BLDG_UPPER = new Sprite32x32("bldglower", "world_foreground.png",
            0x6C, MyColors.YELLOW, TownishSubView.PATH_COLOR, MyColors.RED, MyColors.LIGHT_GRAY);
    private static final Sprite WHEEL_DAY = new WheelSprite(false);
    private static final Sprite WHEEL_NIGHT = new WheelSprite(true);


    public WaterMillNode() {
        super("Miko's Home");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return null;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return BLDG_LOWER;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(BLDG_LOWER.getName(), p, BLDG_LOWER);
        Point p2 = new Point(p.x, p.y - 4);
        model.getScreenHandler().register(BLDG_UPPER.getName(), p2, BLDG_UPPER);
        if (model.getTimeOfDay() == TimeOfDay.EVENING) {
            model.getScreenHandler().register(WHEEL_NIGHT.getName(), p, WHEEL_NIGHT, 1);
        } else {
            model.getScreenHandler().register(WHEEL_DAY.getName(), p, WHEEL_DAY, 1);
        }
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return false;
    }

    @Override
    public Point getCursorShift() {
        return new Point(0, -2);
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {

    }

    private static class WheelSprite extends LoopingSprite {
        public WheelSprite(boolean night) {
            super("waterwheel", "world_foreground.png", 0x5C, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            if (night) {
                setColor2(MyColors.DARK_BROWN);
                setColor3(MyColors.DARK_GRAY);
                setColor4(MyColors.BLUE);
            } else {
                setColor2(MyColors.BROWN);
                setColor3(MyColors.DARK_BROWN);
                setColor4(MyColors.CYAN);
            }
            setDelay(40);
        }
    }
}
