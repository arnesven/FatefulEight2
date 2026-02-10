package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class BuyFoodStandNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("buyfoodstand", "quest.png", 0x69,
            MyColors.GREEN, MyColors.DARK_GRAY, MyColors.GRAY_RED, MyColors.BEIGE);
    private static final Sprite EVENING_SPRITE = new Sprite32x32("buyfoodstand", "quest.png", 0x69,
            MyColors.DARK_GREEN, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.TAN);

    public BuyFoodStandNode() {
        super("Buy Rations");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new BuyRationsState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        if (model.getTimeOfDay() == TimeOfDay.EVENING ||model.getTimeOfDay() == TimeOfDay.NIGHT) {
            model.getScreenHandler().put(p.x, p.y, EVENING_SPRITE);
        } else {
            super.drawYourself(model, p);
        }
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {}
}
