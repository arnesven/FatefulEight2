package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

public class WorkBenchNode extends DailyActionNode {
    private static final Sprite SPRITE1 = new Sprite32x32("workbench", "world_foreground.png", 0x71,
            TownSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.TAN);

    public WorkBenchNode(MyColors groundColor) {
        super("Work Bench");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new CraftItemState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE1;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }
}
