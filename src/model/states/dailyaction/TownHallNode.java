package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.map.UrbanLocation;
import model.states.EveningState;
import model.states.GameState;
import model.states.events.SilentNoEventState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

import java.awt.*;

public class TownHallNode extends DailyActionNode {

    private static final Sprite SPRITE = new Sprite32x32("townhallleft", "world_foreground.png", 0x62,
            TownSubView.STREET_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
    private static final Sprite SPRITE2 = new Sprite32x32("townhallright", "world_foreground.png", 0x63,
            TownSubView.STREET_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);

    public TownHallNode() {
        super("Town Hall");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        if (model.getParty().getSummons().containsKey(((UrbanLocation)model.getCurrentHex().getLocation()).getPlaceName())) {
            state.println("You have been admitted to town hall!");
            return new TownHallDailyActionState(model);
        }
        state.println("You are not admitted to the town hall today.");
        return new SilentNoEventState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().put(p.x, p.y, getBackgroundSprite());
        model.getScreenHandler().put(p.x+4, p.y, SPRITE2);
    }

    @Override
    public Point getCursorShift() {
        Point p = super.getCursorShift();
        p.x += 2;
        return p;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState townDailyActionState, Model model) {
        if (townDailyActionState.isEvening()) {
            townDailyActionState.println("It's too late in the day for that.");
            return false;
        }
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        if (state.isMorning()) {
            model.setTimeOfDay(TimeOfDay.MIDDAY);
        }
    }
}
