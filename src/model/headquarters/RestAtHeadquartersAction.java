package model.headquarters;

import model.Model;
import model.states.GameState;
import model.states.HeadquartersEveningState;

public class RestAtHeadquartersAction extends HeadquartersAction {
    public RestAtHeadquartersAction(Model model) {
        super(model, "Rest");
    }

    @Override
    public GameState run(Model model) {
        if (checkForRations(model, "rest")) {
            return new HeadquartersEveningState(model);
        }
        return null;
    }
}
