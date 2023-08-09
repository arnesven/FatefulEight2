package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.*;

public class CareerOfficeNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("careeroffice", "world_foreground.png", 0x42,
            TownSubView.STREET_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.PINK);


    public CareerOfficeNode() {
        super("Career Office");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new CareerOfficeState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (state.isEvening()) {
            state.println("The shop is closed for today..");
            return false;
        }
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }
}
