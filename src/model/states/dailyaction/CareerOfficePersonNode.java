package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import view.sprites.Sprite;
import view.subviews.CareerOfficeSubView;
import view.subviews.TavernSubView;

import java.awt.*;

public abstract class CareerOfficePersonNode extends DailyActionNode {
    public CareerOfficePersonNode(String name) {
        super(name);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return TavernSubView.FLOOR;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        super.drawYourself(model, p);
        Sprite bar = CareerOfficeSubView.BAR;
        model.getScreenHandler().register(bar.getName(), p, bar);
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        if (state.isMorning()) {
            model.setTimeOfDay(TimeOfDay.MIDDAY);
        }
    }
}
