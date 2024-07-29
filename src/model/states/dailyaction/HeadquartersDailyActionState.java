package model.states.dailyaction;

import model.Model;
import model.headquarters.Headquarters;
import model.states.DailyActionState;
import model.states.GameState;
import model.states.HeadquatersEveningState;
import view.subviews.CollapsingTransition;
import view.subviews.HeadquartersSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.List;

public class HeadquartersDailyActionState extends GameState {
    private final AdvancedDailyActionState previousState;

    public HeadquartersDailyActionState(Model model, AdvancedDailyActionState state) {
        super(model);
        this.previousState = state;
    }

    @Override
    public GameState run(Model model) {
        SubView subView = new HeadquartersSubView();
        CollapsingTransition.transition(model, subView);
        List<String> options = new ArrayList<>(List.of("Leave HQ"));
        if (previousState.isEvening()) {
            options.add(0, "Rest");
        }
        int choice = multipleOptionArrowMenu(model, 24, 24, options);
        if (options.get(choice).contains("Rest")) {
            return new HeadquatersEveningState(model);
        }
        return previousState;
    }
}
