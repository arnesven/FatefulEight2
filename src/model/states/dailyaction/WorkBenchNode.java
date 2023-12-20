package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CastleSubView;
import view.subviews.TownSubView;

public class WorkBenchNode extends DailyActionNode {
    private final Sprite sprite;

    public WorkBenchNode(MyColors groundColor) {
        super("Work Bench");
        sprite = new Sprite32x32("workbenchtown", "world_foreground.png", 0x71,
                groundColor, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.TAN);
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new CraftItemState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return sprite;
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
