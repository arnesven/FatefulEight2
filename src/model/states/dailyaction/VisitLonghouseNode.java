package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.mainstory.GainSupportOfVikingsTask;
import model.map.locations.VikingVillageLocation;
import model.states.GameState;
import model.states.events.NoEventState;
import model.states.events.SilentNoEventState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

import java.awt.*;

public class VisitLonghouseNode extends DailyActionNode {

    private static final Sprite SPRITE1 = new Sprite32x32("longhouseleft", "world_foreground.png", 0xDC,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.WHITE, MyColors.GOLD);
    private static final Sprite SPRITE2 = new Sprite32x32("longhouseright", "world_foreground.png", 0xDD,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.WHITE, MyColors.GOLD);

    public VisitLonghouseNode() {
        super("Visit Longhouse");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        GainSupportOfVikingsTask task = VikingVillageLocation.getVikingTask(model);
        if (task != null) {
            return task.generateEvent(model, true);
        }
        return new SilentNoEventState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE1;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        Point p1 = new Point(p.x - 2, p.y);
        model.getScreenHandler().register(getBackgroundSprite().getName(), p1, getBackgroundSprite());
        Point p2 = new Point(p.x + 2, p.y);
        model.getScreenHandler().register(SPRITE2.getName(), p2, SPRITE2);
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (VikingVillageLocation.getVikingTask(model) == null) {
            state.println("The Longhouse is closed.");
            return false;
        }
        if (model.getTimeOfDay() != TimeOfDay.EVENING) {
            state.println("The Longhouse is during the day. Return in the evening.");
            return false;
        }
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {

    }
}
