package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.states.GameState;
import model.states.TravelBySeaState;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.awt.*;

public class GoTheDocksNode extends DailyActionNode {
    private static Sprite ship = new ShipAtDocksSprite();
    private final TravelBySeaState travelState;

    public GoTheDocksNode(Model model) {
        super("Go to the docks");
        this.travelState = new TravelBySeaState(model);
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return travelState;
    }

    @Override
    public boolean returnNextState() {
        return travelState.didTravel();
    }

    @Override
    public Sprite getBackgroundSprite() {
        return ship;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (state.isEvening()) {
            state.println("All ships have already left for today. Try again tomorrow.");
            return false;
        }
        return true;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        if (model.getTimeOfDay() != TimeOfDay.EVENING) {
            Point shifted = new Point(p);
            shifted.y -= 2;
            model.getScreenHandler().register(ship.getName(), shifted, ship);
        }
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        // Done by state
    }

    private static class ShipAtDocksSprite extends LoopingSprite {

        public ShipAtDocksSprite() {
            super("shipanimation", "world.png", 0x21, 64, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BEIGE);
            setColor3(MyColors.BROWN);
            setFrames(2);
            setDelay(48);
        }
    }
}
