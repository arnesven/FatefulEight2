package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.states.GameState;
import model.states.events.FishingState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class FishingNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("fishingdock", "world_foreground.png", 0xDE,
            MyColors.BLACK, MyColors.BROWN, MyColors.BROWN, MyColors.DARK_GRAY);

    public FishingNode() {
        super("Go Fishing");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new FishingState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return null;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        Point p2 = new Point(p);
        p2.y -= 1;
        model.getScreenHandler().register("objectforeground", p2, SPRITE);
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (model.getTimeOfDay() == TimeOfDay.MORNING || model.getTimeOfDay() == TimeOfDay.MIDDAY) {
            return true;
        }
        state.println("It's too late for that now.");
        return false;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        model.setTimeOfDay(TimeOfDay.MIDDAY);
    }
}
