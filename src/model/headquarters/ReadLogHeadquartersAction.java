package model.headquarters;

import model.Model;
import model.states.GameState;
import view.ReadBookView;

public class ReadLogHeadquartersAction extends HeadquartersAction {
    public ReadLogHeadquartersAction(Model model) {
        super(model, "Read log");
    }

    @Override
    public GameState run(Model model) {
        model.transitionToDialog(new ReadBookView(model, model.getView(),
                model.getParty().getHeadquarters().getLogBook(), true));
        return null;
    }
}
