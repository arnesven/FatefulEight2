package model.actions;

import model.Model;
import model.states.RecruitState;

public class RecruitAction extends DailyAction {
    public RecruitAction(Model model) {
        super("Recruit", new RecruitState(model));
    }
}
