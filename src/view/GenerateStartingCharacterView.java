package view;

import model.Model;
import model.characters.GameCharacter;
import model.states.GameState;

public class GenerateStartingCharacterView extends StartingCharacterView {
    public GenerateStartingCharacterView(Model model) {
        super(model, new GameCharacter[]{GameState.makeRandomCharacter(1)}, false);
    }

    @Override
    protected String getViewTitle() {
        return "GENERATED CHARACTER";
    }
}
