package model.states.dailyaction.tavern;

import model.Model;
import model.TimeOfDay;
import model.races.Race;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TavernSubView;

import java.awt.*;

public class TakeLoanNode extends DailyActionNode {
    private static final Sprite LOAN_SHARK = new Sprite32x32("loanshark", "world_foreground.png", 0x85,
            MyColors.BLACK, MyColors.BROWN, Race.DARK_ELF.getColor());

    public TakeLoanNode() {
        super("Take a loan");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new TakeLoanAction(model);
    }

    public void drawYourself(Model model, Point p) {
        p.y -= 4;
        model.getScreenHandler().register(getForegroundSprite().getName(), new Point(p), getForegroundSprite(), 2);
    }

    @Override
    public Sprite getForegroundSprite() {
        return LOAN_SHARK;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return TavernSubView.FLOOR;
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
