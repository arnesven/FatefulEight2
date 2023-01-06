package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import model.states.RecruitState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TavernSubView;

public class RecruitNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("table", "world_foreground.png", 0x04,
            MyColors.BLACK, MyColors.BROWN, MyColors.BROWN);

    public RecruitNode() {
        super("Recruit Adventurers");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new RecruitState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return TavernSubView.FLOOR;
    }

    @Override
    public Sprite getForegroundSprite() {
        return SPRITE;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (state.isMorning()) {
            return true;
        }
        state.println("All the adventurers have already left for today!");
        return false;
    }

    @Override
    public boolean isFreeAction() {
        return false;
    }
}
