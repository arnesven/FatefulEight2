package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.states.GameState;
import model.states.RecruitState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TavernSubView;

public class RecruitNode extends DailyActionNode {
    public static final Sprite TABLE = new Sprite32x32("table", "world_foreground.png", 0x04,
            MyColors.BLACK, MyColors.TAN, MyColors.BROWN, MyColors.WHITE);
    private RecruitState recruitState;

    public RecruitNode(Model model) {
        super("Recruit Adventurers");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        if (recruitState == null) {
            if (model.getParty().getRecruitmentPersistence() == null) {
                this.recruitState = new RecruitState(model);
            } else {
                this.recruitState = new RecruitState(model, model.getParty().getRecruitmentPersistence());
            }
        }
        return recruitState;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return TavernSubView.FLOOR;
    }

    @Override
    public Sprite getForegroundSprite() {
        return TABLE;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (state.isEvening()) {
            state.println("It's too late in the day for that.");
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
