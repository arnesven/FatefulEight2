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
    private final Sprite daySprite;
    private final Model model;
    private final Sprite32x32 nightSprite;

    public WorkBenchNode(Model model, MyColors dayColor, MyColors nightColor) {
        super("Workbench");
        this.model = model;
        daySprite = new Sprite32x32("workbenchtown", "world_foreground.png", 0x71,
                dayColor, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.TAN);
        nightSprite = new Sprite32x32("workbenchtown", "world_foreground.png", 0x71,
                nightColor, TownSubView.PATH_COLOR, MyColors.BLACK, MyColors.TAN);
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new CraftItemState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        if (model.getTimeOfDay() == TimeOfDay.EVENING) {
            return nightSprite;
        }
        return daySprite;
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
