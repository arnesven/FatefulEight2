package model.headquarters;

import model.Model;
import model.states.GameState;

public abstract class HeadquartersAction extends GameState {
    private final String name;

    public HeadquartersAction(Model model, String name) {
        super(model);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected boolean checkForRations(Model model, String action) {
        Headquarters hq = model.getParty().getHeadquarters();
        if (hq.getShoppers().isEmpty() && hq.getFood() < hq.getCharacters().size()) {
            print("Warning: There are not enough rations in headquarters to feed the " +
                    "characters staying there tonight. Are you sure you want to " + action + "? (Y/N) ");
            return yesNoInput();
        }
        return true;
    }
}
